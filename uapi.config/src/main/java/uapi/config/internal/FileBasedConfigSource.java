package uapi.config.internal;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import com.google.common.base.Strings;

import uapi.InvalidArgumentException;
import uapi.InvalidArgumentException.InvalidArgumentType;
import uapi.KernelException;
import uapi.config.Config;
import uapi.config.IConfigFileParser;
import uapi.internal.TraceableConfigSource;
import uapi.service.IService;
import uapi.service.Inject;

public class FileBasedConfigSource extends TraceableConfigSource implements IService {

    @Inject
    private final Map<String /* file extension */, IConfigFileParser> _parsers;

    public FileBasedConfigSource() {
        this._parsers = new HashMap<>();
    }

    public void addParser(IConfigFileParser parser) {
        if (parser == null) {
            throw new InvalidArgumentException("parser", InvalidArgumentType.EMPTY);
        }
        String[] fileExts = parser.supportedFileExtensions();
        Stream.of(fileExts).forEach((fileExt) -> {
            this._parsers.put(fileExt, parser);
        });
    }

    @Config(qualifier="cli.config")
    public void config(String fileName) {
        if (Strings.isNullOrEmpty(fileName)) {
            throw new InvalidArgumentException("fileName", InvalidArgumentType.EMPTY);
        }
        File cfgFile = new File(fileName);
        if (! cfgFile.exists()) {
            throw new KernelException("The config file {} does not exist.", fileName);
        }
        if (! cfgFile.isFile()) {
            throw new KernelException("The config file {} is not a file.", fileName);
        }
        if (! cfgFile.canRead()) {
            throw new KernelException("The config file {} can't be read.", fileName);
        }
        int posDot = fileName.lastIndexOf('.');
        if (posDot <= 0) {
            throw new KernelException("The config file {} must has a extension name.", fileName);
        }
        String extName = fileName.substring(posDot + 1);
        IConfigFileParser parser = this._parsers.get(extName);
        if (parser == null) {
            throw new KernelException("No parser associate with extension name {} on config file {}.", extName, fileName);
        }
        parser.parse(cfgFile);
    }
}
