package com.vishant.DentalJobVideo.gcm.qb;

import com.vishant.DentalJobVideo.R;
import com.quickblox.sample.core.gcm.CoreGcmPushListenerService;
import com.quickblox.sample.core.utils.NotificationUtils;
import com.quickblox.sample.core.utils.ResourceUtils;
import com.vishant.DentalJobVideo.activity.LoginActivity;
import com.vishant.DentalJobVideo.activity.job_seeker.DashboardJobSeekerActivity;

public class GcmPushListenerService extends CoreGcmPushListenerService {
    private static final int NOTIFICATION_ID = 1;

    @Override
    protected void showNotification(String message) {
        NotificationUtils.showNotification(this, LoginActivity.class,
                ResourceUtils.getString(R.string.notification_title), message,
                R.mipmap.ic_launcher, NOTIFICATION_ID);
    }
}