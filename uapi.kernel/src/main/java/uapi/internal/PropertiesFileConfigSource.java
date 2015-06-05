package uapi.internal;

import java.io.File;
import java.util.Map;

import uapi.service.IService;

public final class PropertiesFileConfigSource implements IService, IConfigFileParser {

    @Override
    public String[] supportedFileExtensions() {
        return new String[] { "properties" };
    }

    @Override
    public Map<String, String> parse(File configFile) {
        return null;
    }

}
