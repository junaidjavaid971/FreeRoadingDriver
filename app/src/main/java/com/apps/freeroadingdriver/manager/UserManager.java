package com.apps.freeroadingdriver.manager;
import com.apps.freeroadingdriver.model.dataModel.Profile;
import com.apps.freeroadingdriver.prefrences.FreeRoadingPreferenceManager;
public class UserManager {
    public static String TAG = UserManager.class.getName();
    private static UserManager instance;
    private Profile user;
    private boolean isLogin;
    private UserManager() {
        this.user = FreeRoadingPreferenceManager.getInstance().getUserDetail();
        this.isLogin = FreeRoadingPreferenceManager.getInstance().isLogin();
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public void setUser(final Profile user) {
        this.user = user;
        FreeRoadingPreferenceManager.getInstance().setUserDetail(user);
    }

    public Profile getUser() {
        return user;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(final boolean login) {
        isLogin = login;
        FreeRoadingPreferenceManager.getInstance().setLogin(login);
    }

    public void loadCache() {

    }

    public void clearCache() {
        instance = null;
    }

    public void logout() {
        instance = null;
        FreeRoadingPreferenceManager.getInstance().setLogin(false);
        FreeRoadingPreferenceManager.getInstance().logoutUser();
    }

    public String isAvailability() {
        return user.getAvailable_status();
    }

    public void setAvailability(String availability) {
        user.setAvailable_status(availability);
        FreeRoadingPreferenceManager.getInstance().setAvailability(availability);
    }

}
