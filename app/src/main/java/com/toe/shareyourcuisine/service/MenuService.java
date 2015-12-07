package com.toe.shareyourcuisine.service;

import android.content.Context;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
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

/**
 * Modified by Theon_Z on 12/6/15.
 * Add comment to menu
 */
public class MenuService {

    private static final String TAG = "ToeMenuService";
    private Context mContext;
    private String mAction;
    private AddMenuListener mAddMenuListener;
    private GetAllMenusListener mGetAllMenusListener;
    private GetSingleMenuListener mGetSingleMenuListener;
    private DeleteMenuListener mDeleteMenuListener;

    public interface AddMenuListener {
        public void addMenuSuccess();
        public void addMenuFail(String errorMsg);
    }

    public interface GetAllMenusListener {
        public void getAllMenusSuccess(List<Menu> menus);
        public void getAllMenusFail(String errorMsg);
    }

    public interface GetSingleMenuListener {
        public void getSingleMenusSuccess(Menu menu);
        public void getSingleMenusFail(String errorMsg);
    }

    public interface DeleteMenuListener {
        public void deleteMenuListenerSuccess(String response);
        public void deleteMenuListenerFail(String errorMsg);
    }

    public MenuService(Context context, Object menuListener, String action) {
        mContext = context;
        if(action.equals("addMenu")) {
            mAddMenuListener = (AddMenuListener)menuListener;
        }
        else if(action.equals("getAllMenus")) {
            mGetAllMenusListener = (GetAllMenusListener)menuListener;
        }
        else if(action.equals("getSingleMenu")) {
            mGetSingleMenuListener = (GetSingleMenuListener)menuListener;
        }
        else if(action.equals("deleteMenu")) {
            mDeleteMenuListener = (DeleteMenuListener)menuListener;
        }
    }

    public void addMenu(Menu menu) {
        ParseObject parseObject = new ParseObject("Menu");
        parseObject.put("title", menu.getmTitle());
        parseObject.put("displayImg", menu.getmDisplayImg());
        parseObject.put("content", menu.getmContent());
        parseObject.put("img", menu.getmImg());
        parseObject.put("createdBy", ParseUser.getCurrentUser());
        parseObject.put("comments",menu.getmComments());
        parseObject.put("cookingTime", menu.getmCookingTime());
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
                if (e == null) {
                    List<Menu> menus = new ArrayList<Menu>();
                    for (int i = 0; i < list.size(); i++) {
                        Menu menu = new Menu();
                        menu.setmObjectId(list.get(i).getObjectId());
                        menu.setmTitle(list.get(i).get("title").toString());
                        menu.setmContent(list.get(i).get("content").toString());
                        menu.setmImg((ArrayList<ParseFile>) list.get(i).get("img"));
                        menu.setmDisplayImg(list.get(i).getParseFile("displayImg"));
                        menu.setmCreatedBy((ParseUser) list.get(i).get("createdBy"));
                        menu.setmCookingTime((String) list.get(i).get("cookingTime"));
                        menus.add(menu);
                    }
                    mGetAllMenusListener.getAllMenusSuccess(menus);
                } else {
                    mGetAllMenusListener.getAllMenusFail(e.getMessage().toString());
                }
            }
        });
    }

    public void getSingleMenu(final String menuId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Menu");
        query.include("createdBy");
        query.include("comments");
        query.whereEqualTo("objectId", menuId);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    Menu menu = new Menu();
                    menu.setmObjectId(parseObject.getObjectId());
                    menu.setmTitle((String) parseObject.get("title"));
                    menu.setmDisplayImg((ParseFile) parseObject.get("displayImg"));
                    menu.setmContent((String) parseObject.get("content"));
                    menu.setmCreatedBy((ParseUser) parseObject.get("createdBy"));
                    menu.setmImg((ArrayList<ParseFile>) parseObject.get("img"));
                    menu.setmComments((ArrayList<ParseObject>) parseObject.get("comments"));
                    mGetSingleMenuListener.getSingleMenusSuccess(menu);
                } else {
                    mGetSingleMenuListener.getSingleMenusFail(e.getMessage().toString());
                }
            }
        });
    }

    public void deleteMenu(String menuId) {
        ParseObject menuObject = ParseObject.createWithoutData("Menu", menuId);
        menuObject.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
                    mDeleteMenuListener.deleteMenuListenerSuccess("Delete menu successfully!");
                }
                else {
                    mDeleteMenuListener.deleteMenuListenerFail(e.getMessage().toString());
                }
            }
        });
    }
}
