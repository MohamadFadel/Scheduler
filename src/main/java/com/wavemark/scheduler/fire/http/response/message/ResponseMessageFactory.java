package com.wavemark.scheduler.fire.http.response.message;

import com.wavemark.scheduler.fire.http.response.HttpResponse;

public class ResponseMessageFactory {

    public static ResponseMessage generateMessage(int responseCode) {

        switch (responseCode) {
            case 200:
                return new ResponseMessage("Success", "Success");
            case 401:
                return new ResponseMessage("Department authentication error",
                        "Task has failed due to an authentication error.");
            case 408:
                return new ResponseMessage("Timeout error",
                        "Task has failed due to a timeout error.");
            case 412:
                return new ResponseMessage("Department configuration error",
                        "Task has failed because your department is not configured to run this task type.");
            case 503:
                return new ResponseMessage("Service unavailable error",
                        "Task has failed due to service unavailability.");
            case 505:
                return new ResponseMessage("Database error",
                        "Task has failed due to a database error.");
            case 506:
                return new ResponseMessage("Database connection error",
                        "Task has failed due to a database connection error.");
            default:
                return new ResponseMessage("Unexpected error", "Task has failed due to an unexpected error.");
        }
    }

    public static ResponseMessage generateMessage(HttpResponse httpResponse) {

        return new ResponseMessage("Code is: " + httpResponse.getCode(), httpResponse.getMessage(), httpResponse.getCause());
    }
}
