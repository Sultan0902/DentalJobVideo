package com.vishant.DentalJobVideo.utils.QB;


import com.vishant.DentalJobVideo.R;
import com.quickblox.sample.core.utils.ResourceUtils;

public interface Consts {

    String SAMPLE_CONFIG_FILE_NAME = "sample_config.json";

    int PREFERRED_IMAGE_SIZE_PREVIEW = ResourceUtils.getDimen(R.dimen.chat_attachment_preview_size);
    int PREFERRED_IMAGE_SIZE_FULL = ResourceUtils.dpToPx(320);
    String QB_USER_PASSWORD = "qb_user_password";


    String EXTRA_QB_USER = "qb_user";

    String EXTRA_USER_ID = "user_id";
    String EXTRA_USER_LOGIN = "user_login";
    String EXTRA_USER_PASSWORD = "user_password";
    String EXTRA_PENDING_INTENT = "pending_Intent";

    int CALL_ACTIVITY_CLOSE_WIFI_DISABLED = 1001;
    String WIFI_DISABLED = "wifi_disabled";
    int MAX_OPPONENTS_COUNT = 6;
    String EXTRA_CONTEXT = "context";
    String EXTRA_OPPONENTS_LIST = "opponents_list";
    String EXTRA_CONFERENCE_TYPE = "conference_type";
    String EXTRA_IS_INCOMING_CALL = "conversation_reason";


    String EXTRA_COMMAND_TO_SERVICE = "command_for_service";
    int COMMAND_NOT_FOUND = 0;
    int COMMAND_LOGIN = 1;
    int COMMAND_LOGOUT = 2;

    String EXTRA_LOGIN_RESULT = "login_result";
    String EXTRA_LOGIN_ERROR_MESSAGE = "login_error_message";
    int EXTRA_LOGIN_RESULT_CODE = 1002;

    String EXTRA_IS_STARTED_FOR_CALL = "isRunForCall";
    String ALREADY_LOGGED_IN = "You have already logged in chat";

    enum StartConversationReason {
        INCOME_CALL_FOR_ACCEPTION,
        OUTCOME_CALL_MADE
    }



}