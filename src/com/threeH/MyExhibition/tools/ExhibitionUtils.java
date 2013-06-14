/*
 * Copyright (C) 2010 Cyril Mottier (http://www.cyrilmottier.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.threeH.MyExhibition.tools;

import com.threeH.MyExhibition.cache.ImageCache;
import com.threeH.MyExhibition.ui.MyApplication;

import java.util.concurrent.ExecutorService;

import android.content.Context;

/**
 * Class that provides several utility methods related to GreenDroid.
 * 
 * @author Cyril Mottier
 */
public class ExhibitionUtils {

    private ExhibitionUtils() {
    }

    /**
     * Return the current {@link com.threeH.MyExhibition.ui.MyApplication}
     * 
     * @param context The calling context
     * @return The {@link com.threeH.MyExhibition.ui.MyApplication} the given context is linked to.
     */
    public static MyApplication getGDApplication(Context context) {
        return (MyApplication) context.getApplicationContext();
    }

    /**
     * Return the {@link com.threeH.MyExhibition.ui.MyApplication} image cache
     * 
     * @param context The calling context
     * @return The image cache of the current {@link com.threeH.MyExhibition.ui.MyApplication}
     */
    public static ImageCache getImageCache(Context context) {
        return getGDApplication(context).getImageCache();
    }

    /**
     * Return the {@link com.threeH.MyExhibition.ui.MyApplication} executors pool.
     * 
     * @param context The calling context
     * @return The executors pool of the current {@link com.threeH.MyExhibition.ui.MyApplication}
     */
    public static ExecutorService getExecutor(Context context) {
        return getGDApplication(context).getExecutor();
    }

}
