/*
 * Copyright (C) 2010 The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.sample.async;

import uapi.service.annotation.Service;
import uapi.service.annotation.Tag;

/**
 * Created by xquan on 9/20/2016.
 */
@Service
@Tag("Async")
public class RealService {

    public String getTitle(String name) {
        return "Test";
    }
}
