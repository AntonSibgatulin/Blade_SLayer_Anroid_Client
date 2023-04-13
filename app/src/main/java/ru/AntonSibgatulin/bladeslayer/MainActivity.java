package ru.AntonSibgatulin.bladeslayer;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ConfigurationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import ru.AntonSibgatulin.bladeslayer.Players.InfoPlayerFragment;
import ru.AntonSibgatulin.bladeslayer.Players.PlayerController;
import ru.AntonSibgatulin.bladeslayer.battle.LocationFragment;
import ru.AntonSibgatulin.bladeslayer.battle.MapModelFragment;
import ru.AntonSibgatulin.bladeslayer.battle.TrainFragment;
import ru.AntonSibgatulin.bladeslayer.battle.WindowPlayFragment;
import ru.AntonSibgatulin.bladeslayer.captcha.CaptchaFragment;
import ru.AntonSibgatulin.bladeslayer.clientconnection.ClientWebSocket;
import ru.AntonSibgatulin.bladeslayer.help.HelpFragment;
import ru.AntonSibgatulin.bladeslayer.loader.ImageLoader;
import ru.AntonSibgatulin.bladeslayer.loader.JSONLoader;
import ru.AntonSibgatulin.bladeslayer.loader.LoaderFromAssets;
import ru.AntonSibgatulin.bladeslayer.register.RegFragment;
import ru.AntonSibgatulin.bladeslayer.restart.RestartFragment;
import ru.AntonSibgatulin.bladeslayer.shop.ShopLoadingActivityFragment;

public class MainActivity extends AppCompatActivity {
    public static Fragment loader = null;
    public static final String data_for_automaticly_login = "data";
    public static PlayerController playerControllerStatic = null;

    public String ip = "299097.simplecloud.ru";

    public LocationFragment locationFragment = null;

    // public GLSurfaceView glSurfaceView = null;
    public boolean rendererSet = false;

    public Config config = null;//new Config();
    public static ClientWebSocket connection = null;
    public SharedPreferences sharedPreferences = null;

    public LobbyFragment lobbyFragment = null;
    public RegFragment regFragment = null;
    public LoginFragment loginFragment = null;
    public CaptchaFragment captchaFragment = null;


    public Point sizeWindow = new Point();
    public LoaderFromAssets loaderFromAssets = new LoaderFromAssets();
    public ImageLoader imageLoader = new ImageLoader();


    public PlayerController playerController = null;
    public LocaleHelper localeHelper = new LocaleHelper();

    public String[] rangArray = {"Ученик", "Студент", "Младьший охотник", "Охотник", "Старший охотник", "Помошник хашира", "Младший хашира", "Хашира", "Хашира - охотник на демонов", "Столп"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // glSurfaceView = new GLSurfaceView(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Log.v(TAG,"Permission is granted");
                //File write logic here
                //  return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        String country = Locale.getDefault().getCountry();
      //  Log.e("country", country);
        if (country.contains("RU")) {
            country = "ru";
        } else if (country.contains("ES")) {
            country = "es";
            rangArray = new String[]{
                    "Alumno",
                    "Alumno",
                    "Cazador menor",
                    "Cazador",
                    "Cazador superior",
                    "Ayudante de Hashira",
                    "Junior Hashira",
                    "Hashira",
                    "Hashira el cazador de demonios",
                    "Pilar"
            };
        } else if (country.contains("JA")) {
            country = "ja";
            rangArray = new String[]{
                    "学生",
                    "学生",
                    "ジュニアハンター",
                    "猟師",
                    "シニアハンター",
                    "柱の助っ人",
                    "ジュニアハシラ",
                    "はしら",
                    "魔狩人のハシラ",
                    "柱"};
        } else if (country.contains("EN")) {
            country = "en";
            rangArray = new String[]{
                    "Apprentice",
                    "Student",
                    "Junior Hunter",
                    "Hunter",
                    "Senior Hunter",
                    "Hashir's Helper",
                    "Junior Hashira",
                    "Hashira",
                    "Hashira the Demon Hunter",
                    "Pillar"
            };
        } else if (country.contains("ZH")) {
            country = "zh";
            rangArray = new String[]{
                    "学生",
                    "学生",
                    "初级猎人",
                    "猎人",
                    "高级猎手",
                    "柱的助手",
                    "小柱子",
                    "柱",
                    "恶魔猎手柱",
                    "支柱"};
        } else {
            country = "en";
            rangArray = new String[]{
                    "Apprentice",
                    "Student",
                    "Junior Hunter",
                    "Hunter",
                    "Senior Hunter",
                    "Hashir's Helper",
                    "Junior Hashira",
                    "Hashira",
                    "Hashira the Demon Hunter",
                    "Pillar"
            };
        }
        //Log.e("country code", country);
        String lan = "_" + country;
        if (country.equals("ru")) {
            lan = "";
        }else if(country.equals("zh")){

        }
        else if (country.equals("es")) {

        } else if (country.equals("ja")) {
           // lan = "_jp";
        } else if (country.equals("en")) {

        } else {
            country = "en";
            lan = "_en";
        }
        Locale locale = new Locale(country);

        Locale.setDefault(locale);
// Create a new configuration object
        Configuration configure = new Configuration();
// Set the locale of the new configuration
        configure.locale = locale;
// Update the configuration of the Accplication context
        getResources().updateConfiguration(
                configure,
                getResources().getDisplayMetrics()
        );


        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;
        if (supportsEs2) {

            Toast.makeText(this, "This device support OpenGL ES 2.0.", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, "This device does not support OpenGL ES 2.0.", Toast.LENGTH_LONG).show();

            return;

        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        String url = "ws://" + ip + ":9999";
        String url_site = "http://" + ip + "/play/";
        config = new Config();

        if (connection == null)
            connection = new ClientWebSocket(this, url, url_site, this, playerController, config);


        getWindowManager().getDefaultDisplay().getSize(sizeWindow);
        if (sizeWindow.x < sizeWindow.y) {
            sizeWindow = new Point(sizeWindow.y, sizeWindow.x);
        }
        double width = sizeWindow.x;

        config.scale = 2.1;
        config.dt = (1920.0 / width) / config.scale;
        double d = 1920.0 / width;
        config.SIZE = (int) Math.floor(config.START_SIZE / (d) * config.scale);
        config.hightTop = 1.4;
        config.innerWidth = sizeWindow.x;
        config.innerHeight = sizeWindow.y;
        config.lan = lan;
        config.country = country;
        runMain();


        //if(true == true)return;

        if (loadData() != null && !loadData().isEmpty()) {

            connection.save_data = loadData();

            // setLandscapeMode();
            //startLogin();
            //runMain();
            //Log.e("Data", connection.save_data + ";" + connection.config.country);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            Thread.sleep(1500);
                            if (getBaseContext() != null && connection.isOpened) {

                                connection.send("login;" + loadData() + ";" + connection.config.country);
                                break;
                            }

                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }).start();

        } else {
            startLogin();
            // runMain();
        }


        //loadingWarningResources();

    }

    public void initRegModel() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                setNoLandscapeMode();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setReorderingAllowed(true);
                regFragment = new RegFragment();
// Replace whatever is in the fragment_container view with this fragment
                transaction.replace(R.id.fragmentMain, regFragment, null);

// Commit the transaction
                transaction.commit();

            }
        });

    }


    public void blockApplication() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                setNoLandscapeMode();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setReorderingAllowed(true);
                RestartFragment restartFragment = new RestartFragment();
// Replace whatever is in the fragment_container view with this fragment
                transaction.replace(R.id.fragmentMain, restartFragment, null);

// Commit the transaction
                transaction.commit();

            }
        });

    }

    public void initCaptchaModel(Bitmap bitmap) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                setNoLandscapeMode();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setReorderingAllowed(true);
                captchaFragment = new CaptchaFragment(bitmap);
// Replace whatever is in the fragment_container view with this fragment
                transaction.replace(R.id.fragmentMain, captchaFragment, null);

// Commit the transaction
                transaction.commit();

            }
        });

    }

    public void loadingWarningResources() {


        JSONObject jsonObject = JSONLoader.jsonObjectLoadeFromURLStatic(connection.site + "res/index.json");// mainActivity.loaderFromAssets.getStringFromAssetFile(mainActivity, "res/index.json");
        JSONObject jsonObject1 = JSONLoader.jsonObjectLoadeFromURLStatic(connection.site + "res/index.json");// mainActivity.loaderFromAssets.getStringFromAssetFile(mainActivity, "res/index.json");
        if (jsonObject == null) {
            startLogin();
            return;
        }

        // JSONObject jsonObject = null;
        try {

            //jsonObject = new JSONObject(string);
            long timemain = System.currentTimeMillis();
            Iterator<String> iterator = jsonObject.keys();
            Iterator<String> iterator1 = jsonObject.keys();
            int all = 1;

            while (iterator1.hasNext()) {
                iterator1.next();
                all++;
            }


            int count = 0;
            connection.isLoading = true;
            runLoadingFragment();
            while (iterator.hasNext()) {
                count++;

                String key = iterator.next();
                if (jsonObject.get(key) instanceof String) {
                    String url1 = jsonObject.getString(key);
                    if (url1.startsWith("/") && url1.length() > 1) {
                        url1.replace("/", "");
                    }
                    long time = System.currentTimeMillis();
                    imageLoader.loadFromUrlImage(connection.site + "" + url1, key, connection.version, MainActivity.this);
//                                    Log.e("data",(System.currentTimeMillis()-time)+" "+site+""+url+" "+bitmap.getWidth());

                }
                connection.percent = count * 100 / all;

            }
            connection.isLoading = false;
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    public void setLandscapeMode() {
        // int currentOrientation = this.getResources().getConfiguration().orientation;
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


    }

    public void setNoLandscapeMode() {
        // int currentOrientation = this.getResources().getConfiguration().orientation;
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


    }

    public void runMain() {

        setContentView(R.layout.activity_main);
        runALLConfig();


    }

    public void runALLConfig() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    public void runMainLoading() {
        setContentView(R.layout.activity_main);

    }

    public String loadData() {
        sharedPreferences = getPreferences(MODE_PRIVATE);
        String savedText = sharedPreferences.getString(data_for_automaticly_login, "");
        return savedText;


    }

    public void saveData(String text) {
        sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(data_for_automaticly_login, text);
        editor.commit();

    }

    public String loadInfo(String key) {
        sharedPreferences = getPreferences(MODE_PRIVATE);
        String savedText = sharedPreferences.getString(key, "");
        return savedText;
    }

    public void saveInfo(String key, String value) {
        sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void runShop() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        setLandscapeMode();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.setReorderingAllowed(true);

// Replace whatever is in the fragment_container view with this fragment
                        transaction.replace(R.id.fragmentMain, ShopFragment.class, null);

// Commit the transaction
                        transaction.commit();
                        removeLoading();
                    }
                });


            }
        }).start();


    }

    public void startMenuWithNotification(final String string) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                startLoadingShop();
                try {
                    Thread.sleep(100);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            setLandscapeMode();
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.setReorderingAllowed(true);

// Replace whatever is in the fragment_container view with this fragment
                            lobbyFragment = new LobbyFragment();
                            transaction.replace(R.id.fragmentMain, lobbyFragment, null);

// Commit the transaction
                            transaction.commit();
                            lobbyFragment.addNotificationWindow(string);
                            removeLoading();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }


    public void startMenu() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                startLoadingShop();
                try {
                    Thread.sleep(100);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            setLandscapeMode();
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.setReorderingAllowed(true);

// Replace whatever is in the fragment_container view with this fragment
                            lobbyFragment = new LobbyFragment();
                            transaction.replace(R.id.fragmentMain, lobbyFragment, null);

// Commit the transaction
                            transaction.commit();
                            removeLoading();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }


    public void startMenuEnd(String[] str) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                startLoadingShop();
                try {
                    Thread.sleep(100);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            setLandscapeMode();
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.setReorderingAllowed(true);

// Replace whatever is in the fragment_container view with this fragment
                            lobbyFragment = new LobbyFragment();
                            transaction.replace(R.id.fragmentMain, lobbyFragment, null);

// Commit the transaction
                            transaction.commit();
                            removeLoading();
                            lobbyFragment.addNotificationWindow("You winer! :) \n+" + str[2] + " " + getResources().getString(R.string.TEXT_SCORE) + "\n+" + str[3] + " " + getResources().getString(R.string.TEXT_MONEY));
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }


    public void startLogin() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                startLoadingShop(false);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setNoLandscapeMode();
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {


                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.setReorderingAllowed(true);
                        loginFragment = new LoginFragment();
// Replace whatever is in the fragment_container view with this fragment
                        transaction.replace(R.id.fragmentMain, loginFragment, null);

// Commit the transaction
                        transaction.commit();
               /*new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                */
                        removeLoading();
/*
                    }
                }).start();

 */
                    }
                });
            }
        }).start();


    }


    public void startLoginWithNotification(String text) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                startLoadingShop(false);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setNoLandscapeMode();
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {


                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.setReorderingAllowed(true);
                        loginFragment = new LoginFragment();
// Replace whatever is in the fragment_container view with this fragment
                        transaction.replace(R.id.fragmentMain, loginFragment, null);

// Commit the transaction
                        transaction.commit();
                        loginFragment.addNotificationWindow(text);
               /*new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                */
                        removeLoading();
/*
                    }
                }).start();

 */
                    }
                });
            }
        }).start();


    }

    public void runLoadingFragment() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                setLandscapeMode();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setReorderingAllowed(true);

// Replace whatever is in the fragment_container view with this fragment
                transaction.replace(R.id.fragmentMain, LoadingFragment.class, null).commit();

// Commit the transaction

            }
        });

    }

    public static void setTextViewItalic(String text, TextView view) {
        SpannableString spanString = new SpannableString(text);
        spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
        spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
        spanString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spanString.length(), 0);
        view.setText(spanString);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void startLoadingShop() {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                setLandscapeMode();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setReorderingAllowed(true);

                transaction.replace(R.id.fragmentMainLoading, ShopLoadingActivityFragment.class, null);

                transaction.commit();

            }
        });
    }

    public void startLoadingShop(boolean land) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (land) {
                    setLandscapeMode();
                }
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setReorderingAllowed(true);

                transaction.replace(R.id.fragmentMainLoading, ShopLoadingActivityFragment.class, null);

                transaction.commit();

            }
        });
    }

    public void removeLoading(Fragment fragment) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                setLandscapeMode();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setReorderingAllowed(true);

                transaction.remove(fragment);

                transaction.commit();

            }
        });
    }


    public void removeLoading() {
        // startLoadingShop();
        if (loader == null) return;
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                //    setLandscapeMode();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setReorderingAllowed(true);

                transaction.remove(loader);
                loader = null;
                transaction.commit();

            }
        });
    }

    public void runleveldata(String name, JSONObject jsonObject2, Bitmap bitmap) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                startLoadingShop();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        setLandscapeMode();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.setReorderingAllowed(true);
                        InfoPlayerFragment infoPlayerFragment = new InfoPlayerFragment(name, name, jsonObject2, bitmap);

// Replace whatever is in the fragment_container view with this fragment
                        transaction.replace(R.id.fragmentMain, infoPlayerFragment, null);

// Commit the transaction
                        transaction.commit();
                        removeLoading();
                    }
                });
            }
        }).start();

    }

    public void runupgradedata(String name, JSONObject jsonObject2, Bitmap bitmap) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                startLoadingShop();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        setLandscapeMode();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.setReorderingAllowed(true);
                        UpgradeMaximalFragment infoPlayerFragment = new UpgradeMaximalFragment(name, name, jsonObject2, bitmap);

// Replace whatever is in the fragment_container view with this fragment
                        transaction.replace(R.id.fragmentMain, infoPlayerFragment, null);

// Commit the transaction
                        transaction.commit();
                        removeLoading();
                    }
                });
            }
        }).start();

    }

    public void runupgradedatamaximal(String name, JSONObject jsonObject2, Bitmap bitmap) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                startLoadingShop();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        setLandscapeMode();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.setReorderingAllowed(true);
                        MaximalUpgradeFragment infoPlayerFragment = new MaximalUpgradeFragment(name, name, jsonObject2, bitmap);

// Replace whatever is in the fragment_container view with this fragment
                        transaction.replace(R.id.fragmentMain, infoPlayerFragment, null);

// Commit the transaction
                        transaction.commit();
                        removeLoading();
                    }
                });
            }
        }).start();

    }

    public void runPlayWindow(int type, String description, Bitmap map, String types) {
        startLoadingShop();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                setLandscapeMode();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setReorderingAllowed(true);
                WindowPlayFragment windowPlayFragment = new WindowPlayFragment(type, description, map, types);
// Replace whatever is in the fragment_container view with this fragment
                transaction.replace(R.id.fragmentMain, windowPlayFragment, null);

// Commit the transaction
                transaction.commit();
                removeLoading();
            }
        });
    }

    public void runTrainWindow(int type, String description, Bitmap map) {
        startLoadingShop();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                setLandscapeMode();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setReorderingAllowed(true);
                TrainFragment windowPlayFragment = new TrainFragment(type, description, map);
// Replace whatever is in the fragment_container view with this fragment
                transaction.replace(R.id.fragmentMain, windowPlayFragment, null);

// Commit the transaction
                transaction.commit();
                removeLoading();
            }
        });
    }

    public void runHelpWindow() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                startLoadingShop();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        setLandscapeMode();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();

                        transaction.setReorderingAllowed(true);
                        HelpFragment helpFragment = new HelpFragment("http://" + ip + "/play/about.php?locale="+config.lan);
// Replace whatever is in the fragment_container view with this fragment
                        transaction.replace(R.id.fragmentMain, helpFragment, null);

// Commit the transaction
                        transaction.commit();
                         removeLoading();
                    }
                });


            }
        }).start();

    }

    public void play() {
        connection.send("lobby;play;" + sizeWindow.x + ";" + sizeWindow.y);
    }

    public void playTrain() {
        connection.send("lobby;train;" + sizeWindow.x + ";" + sizeWindow.y);
    }

    public void train() {
        connection.send("lobby;train_get;" + sizeWindow.x + ";" + sizeWindow.y);
    }

    public void playInit(ArrayList<ArrayList<Integer>> arr) {
        startLoadingShop();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                setLandscapeMode();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setReorderingAllowed(true);
                getWindowManager().getDefaultDisplay().getSize(sizeWindow);

                MapModelFragment mapModelFragment = new MapModelFragment(sizeWindow, connection, arr);
                //  MapFragment mapModelFragment = new MapFragment();
                //    WindowPlayFragment windowPlayFragment = new WindowPlayFragment(type,description,map);
// Replace whatever is in the fragment_container view with this fragment
                transaction.replace(R.id.fragmentMain, mapModelFragment, null);

// Commit the transaction
                transaction.commit();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        removeLoading();
                    }
                }).start();

            }
        });
    }


    public void runLocationFragment(JSONObject jsonObject) {
        startLoadingShop();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                setLandscapeMode();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setReorderingAllowed(true);
                getWindowManager().getDefaultDisplay().getSize(sizeWindow);
                LocationFragment locationFragment = new LocationFragment(jsonObject);
                transaction.replace(R.id.fragmentMain, locationFragment, null);
                MainActivity.this.locationFragment = locationFragment;

                transaction.commit();


                removeLoading();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

                }
                break;
        }
    }
}