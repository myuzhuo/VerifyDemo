package com.example.record.demo.utils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.orhanobut.logger.Logger;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * 处理Intent数据获取
 *
 * @author colinsLin
 */
public class IntentUtils {

    private IntentUtils() {
    }

    public static int getIntExtra(Intent intent, String key, int defaultValue) {
        if (intent != null) {
            return getIntExtra(intent.getExtras(), key, defaultValue);
        }
        return defaultValue;
    }

    public static int getIntExtra(Bundle bundle, String key, int defaultValue) {
        try {
            if (bundle != null && bundle.containsKey(key) && bundle.get(key) != null && bundle.get(key) instanceof Integer) {
                return bundle.getInt(key, defaultValue);
            }
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
        return defaultValue;
    }

    public static String getStringExtra(Bundle bundle, String key) {
        return getStringExtra(bundle, key, "");
    }

    public static String getStringExtra(Intent intent, String key, String defaultValue) {
        if (intent != null) {
            return getStringExtra(intent.getExtras(), key, defaultValue);
        }
        return defaultValue;
    }

    public static String getStringExtra(Bundle bundle, String key, String defaultValue) {
        try {
            if (bundle != null && bundle.containsKey(key) && bundle.get(key) != null && bundle.get(key) instanceof String) {
                return bundle.getString(key);
            }
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
        return defaultValue;
    }


    public static String[] getStringArray(Bundle bundle, String key) {
        try {
            if (bundle != null && bundle.containsKey(key) && bundle.get(key) != null && bundle.get(key) instanceof String[]) {
                return bundle.getStringArray(key);
            }
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
        return null;
    }

    public static int[] getIntArray(Bundle bundle, String key) {
        try {
            if (bundle != null && bundle.containsKey(key) && bundle.get(key) != null && bundle.get(key) instanceof int[]) {
                return bundle.getIntArray(key);
            }
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
        return null;
    }

    public static long getLongExtra(Bundle bundle, String key, long defaultValue) {
        try {
            if (bundle != null && bundle.containsKey(key) && bundle.get(key) != null && bundle.get(key) instanceof Long) {
                return bundle.getLong(key, defaultValue);
            }
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
        return defaultValue;
    }

    public static long getLongExtra(Intent intent, String key, long defaultValue) {
        try {
            if (intent != null && intent.getExtras() != null) {
                return getLongExtra(intent.getExtras(), key, defaultValue);
            }
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
        return defaultValue;
    }

    public static boolean getBooleanExtra(Bundle bundle, String key, boolean defaultValue) {
        try {
            if (bundle != null && bundle.containsKey(key) && bundle.get(key) != null && bundle.get(key) instanceof Boolean) {
                return bundle.getBoolean(key, defaultValue);
            }
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
        return defaultValue;
    }

    public static boolean getBooleanExtra(Intent intent, String key, boolean defaultValue) {
        try {
            if (intent != null && intent.getExtras() != null
                    && intent.getExtras().containsKey(key)
                    && intent.getExtras().get(key) != null
                    && intent.getExtras().get(key) instanceof Boolean) {
                return intent.getExtras().getBoolean(key, defaultValue);
            }
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
        return defaultValue;
    }

    public static double getDoubleExtra(Bundle bundle, String key, double defaultValue) {
        try {
            if (bundle != null && bundle.containsKey(key) && bundle.get(key) != null && bundle.get(key) instanceof Double) {
                return bundle.getDouble(key, defaultValue);
            }
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
        return defaultValue;
    }

    public static double getDoubleExtra(Intent intent, String key, double defaultValue) {
        try {
            if (intent != null && intent.getExtras() != null
                    && intent.getExtras().containsKey(key)
                    && intent.getExtras().get(key) != null
                    && intent.getExtras().get(key) instanceof Double) {
                return intent.getExtras().getDouble(key, defaultValue);
            }
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
        return defaultValue;
    }

    public static float getFloatExtra(Bundle bundle, String key, float defaultValue) {
        try {
            if (bundle != null && bundle.containsKey(key) && bundle.get(key) != null && bundle.get(key) instanceof Float) {
                return bundle.getFloat(key, defaultValue);
            }
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
        return defaultValue;
    }

    public static Serializable getSerializable(Bundle bundle, String key) {
        try {
            if (bundle != null && bundle.containsKey(key) && bundle.get(key) != null
                    && bundle.get(key) instanceof Serializable) {
                return bundle.getSerializable(key);
            }
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
        return null;
    }

    public static Parcelable getParcelable(Bundle bundle, String key) {
        try {
            if (bundle != null && bundle.containsKey(key) && bundle.get(key) != null
                    && bundle.get(key) instanceof Parcelable) {
                return bundle.getParcelable(key);
            }
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
        return null;
    }

    public static <T extends Parcelable> ArrayList<T> getParcelableArrayList(Bundle bundle, String key) {
        try {
            if (bundle != null && bundle.containsKey(key) && bundle.get(key) != null
                    && bundle.get(key) instanceof ArrayList) {
                return bundle.getParcelableArrayList(key);
            }
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
        return null;
    }

    public static <T extends Parcelable> ArrayList<T> getParcelableArrayList(Intent intent, String key) {
        try {
            if (intent != null && intent.getExtras() != null
                    && intent.getExtras().containsKey(key)
                    && intent.getExtras().get(key) != null
                    && intent.getExtras().get(key) instanceof ArrayList) {
                return intent.getExtras().getParcelableArrayList(key);
            }
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
        return null;
    }

    public static <T extends Parcelable> T getParcelable(Intent intent, String key) {
        try {
            if (intent != null && intent.getExtras() != null
                    && intent.getExtras().containsKey(key)
                    && intent.getExtras().get(key) != null
                    && intent.getExtras().get(key) instanceof Parcelable) {
                return intent.getExtras().getParcelable(key);
            }
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
        return null;
    }

}
