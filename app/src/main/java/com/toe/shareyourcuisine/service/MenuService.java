package com.toe.shareyourcuisine.service;

import android.content.Context;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.toe.shareyourcuisine.model.Menu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TommyQu on 12/2/15.
 */
public class MenuService {

    private static final String TAG = "ToeMenuService";
    private Context mContext;
    private String mAction;
    private AddMenuListener mAddMenuListener;
    private GetAllMenusListener mGetAllMenusListener;

    public interface AddMenuListener {
        public void addMenuSuccess();
        public void addMenuFail(String errorMsg);
    }

    public interface GetAllMenusListener {
        public void getAllMenusSuccess(List<Menu> menus);
        public void getAllMenusFail(String errorMsg);
    }

    public MenuService(Context context, Object menuListener, String action) {
        mContext = context;
        if(action.equals("addMenu")) {
            mAddMenuListener = (AddMenuListener)menuListener;
        }
        else if(action.equals("getAllMenus")) {
            mGetAllMenusListener = (GetAllMenusListener)menuListener;
        }
    }

    public void addMenu(Menu menu) {
        ParseObject parseObject = new ParseObject("Menu");
        parseObject.put("title", menu.getmTitle());
        parseObject.put("displayImg", menu.getmDisplayImg());
        menu.getmDisplayImg().saveInBackground();
        parseObject.put("content", menu.getmContent());
        parseObject.put("img", menu.getmImg());
        parseObject.put("createdBy", ParseUser.getCurrentUser());
        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null)
                    mAddMenuListener.addMenuSuccess();
                else
                    mAddMenuListener.addMenuFail(e.getMessage().toString());
            }
        });
    }

    public void getAllMenus() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Menu");
        query.include("createdBy");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if(e == null) {
                    List<Menu> menus = new ArrayList<Menu>();
                    for(int i = 0; i < list.size(); i++) {
                        Menu menu = new Menu();
                        menu.setmTitle(list.get(i).get("title").toString());
                        menu.setmDisplayImg(list.get(i).getParseFile("displayImg"));
                        menus.add(menu);
                    }
                    mGetAllMenusListener.getAllMenusSuccess(menus);
                }
                else {
                    mGetAllMenusListener.getAllMenusFail(e.getMessage().toString());
                }
            }
        });
    }
}
