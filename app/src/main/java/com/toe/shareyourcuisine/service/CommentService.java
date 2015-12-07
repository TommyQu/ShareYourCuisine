package com.toe.shareyourcuisine.service;

import android.content.Context;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.toe.shareyourcuisine.model.Comment;

/**
 * Created by Theon_Z on 12/6/15.
 */
public class CommentService {

    private static final String TAG = "ToeCommentService";
    private Context mContext;
    private AddCommentToPostListener mAddCommentToPostListener;
    private AddCommentToMenuListener mAddCommentToMenuListener;

    public interface AddCommentToMenuListener {
        public void addCommentToMenuSuccess();
        public void addCommentToMenuFail(String errorMsg);
    }

    public interface AddCommentToPostListener {
        public void addCommentToPostSuccess();
        public void addCommentToPostFail(String errorMsg);
    }

    public CommentService(Context context, Object activityListener, String action) {
        mContext = context;
        if (action.equals("addCommentToPost")) {
            mAddCommentToPostListener = (AddCommentToPostListener)activityListener;
        }
        else if (action.equals("addCommentToMenu")) {
            mAddCommentToMenuListener = (AddCommentToMenuListener)activityListener;
        }
    }


    public void addCommentToPost(final String postId, final Comment comment) {
        final ParseObject commentObj = new ParseObject("Comment");
        commentObj.put("content",comment.getContent());
        commentObj.put("createdBy",comment.getCreatedBy());
        commentObj.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if( e == null) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
                    query.whereEqualTo("objectId", postId);
                    query.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject parseObject, ParseException e) {
                            if(e == null) {
                                parseObject.addUnique("comments", commentObj);
                                parseObject.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null)
                                            mAddCommentToPostListener.addCommentToPostSuccess();
                                        else
                                            mAddCommentToPostListener.addCommentToPostFail(e.getMessage().toString());
                                    }
                                });
                            } else {
                                mAddCommentToPostListener.addCommentToPostFail(e.getMessage().toString());
                            }
                        }
                    });
                } else {
                    mAddCommentToPostListener.addCommentToPostFail(e.getMessage().toString());
                }
            }
        });
    }

    public void addCommentToMenu(final String menuId, final Comment comment) {
        final ParseObject commentObj = new ParseObject("Comment");
        commentObj.put("content",comment.getContent());
        commentObj.put("createdBy",comment.getCreatedBy());
        commentObj.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if( e == null) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Menu");
                    query.whereEqualTo("objectId", menuId);
                    query.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject parseObject, ParseException e) {
                            if(e == null) {
                                parseObject.addUnique("comments", commentObj);
                                parseObject.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null)
                                            mAddCommentToMenuListener.addCommentToMenuSuccess();
                                        else
                                            mAddCommentToMenuListener.addCommentToMenuFail(e.getMessage().toString());
                                    }
                                });
                            } else {
                                mAddCommentToMenuListener.addCommentToMenuFail(e.getMessage().toString());
                            }
                        }
                    });
                } else {
                    mAddCommentToMenuListener.addCommentToMenuFail(e.getMessage().toString());
                }
            }
        });
    }
}

