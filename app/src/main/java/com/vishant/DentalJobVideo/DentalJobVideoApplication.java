package com.vishant.DentalJobVideo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDex;

import com.quickblox.messages.services.SubscribeService;
import com.quickblox.sample.core.CoreApp;
import com.quickblox.sample.core.utils.ActivityLifecycle;
import com.quickblox.sample.core.utils.SharedPrefsHelper;
import com.vishant.DentalJobVideo.model.QB.ChatConfigs;
import com.vishant.DentalJobVideo.model.EmployerData;
import com.vishant.DentalJobVideo.model.JobSeekerData;
import com.vishant.DentalJobVideo.utils.QB.Consts;
import com.vishant.DentalJobVideo.utils.QB.calling.QBResRequestExecutor;
import com.vishant.DentalJobVideo.utils.QB.chat.ChatHelper;
import com.vishant.DentalJobVideo.utils.QB.configs.ConfigUtils;
import com.vishant.DentalJobVideo.utils.QB.qb.QbDialogHolder;

import java.io.IOException;

/**
 * Created by Sultan Ahmed on 3/3/2017.
 */
public class DentalJobVideoApplication extends CoreApp {
    private static final String TAG = DentalJobVideoApplication.class.getSimpleName();
    private static ChatConfigs sampleConfigs;
    private static String userType;
    private static EmployerData employerData;
    private static JobSeekerData jobSeekerData;
    private static QBResRequestExecutor qbResRequestExecutor;
    @Override
    public void onCreate() {
        super.onCreate();
        ActivityLifecycle.init(this);
        initSampleConfigs();
    }

    private void initSampleConfigs() {
        try {
            sampleConfigs = ConfigUtils.getSampleConfigs(Consts.SAMPLE_CONFIG_FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ChatConfigs getSampleConfigs() {
        return sampleConfigs;
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static synchronized QBResRequestExecutor getQbResRequestExecutor() {
        return qbResRequestExecutor == null
                ? qbResRequestExecutor = new QBResRequestExecutor()
                : qbResRequestExecutor;
    }

    public static JobSeekerData getJobSeekerData() {
        return jobSeekerData;
    }

    public static void setJobSeekerData(JobSeekerData jobSeekerData) {
        DentalJobVideoApplication.jobSeekerData = jobSeekerData;
    }

    public static EmployerData getEmployerData() {
        return employerData;
    }

    public static void setEmployerData(EmployerData employerData) {
        DentalJobVideoApplication.employerData = employerData;
    }

    public static String getUserType() {
        return userType;
    }

    public static void setUserType(String userType) {
        DentalJobVideoApplication.userType = userType;
    }

    public static void clearSavedUser(Context context){
        SharedPreferences.Editor editor = getSharedPreferenceEditor(context);
        editor.clear();
        editor.commit();
        ChatHelper.getInstance().destroy();
        SubscribeService.unSubscribeFromPushes(context);
        SharedPrefsHelper.getInstance().removeQbUser();
        QbDialogHolder.getInstance().clear();
    }
    public static SharedPreferences.Editor getSharedPreferenceEditor(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        return editor;
    }

}
