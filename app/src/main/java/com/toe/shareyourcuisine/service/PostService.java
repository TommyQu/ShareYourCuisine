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
import com.toe.shareyourcuisine.model.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Theon_Z on 10/31/15.
 */
public class PostService {
    private static final String TAG = "ToePostService";
    private Context mContext;
    private String mAction;
    private AddPostListener mAddPostListener;
    private GetAllPostsListener mGetAllPostsListener;
    private GetSinglePostListener mGetSinglePostListener;
    private DeletePostListener mDeletePostListener;

    public interface GetAllPostsListener {
        public void getAllPostsSuccess(List<Post> postlist);
        public void getAllPostsFail(String errorMsg);
    }

    public interface GetSinglePostListener {
        public void getSinglePostSuccess(Post post);
        public void getSinglePostFail(String errorMsg);
    }

    public interface AddPostListener {
        public void addPostSuccess();
        public void addPostFail(String errorMsg);
    }

    public interface DeletePostListener {
        public void deletePostListenerSuccess(String response);
        public void deletePostListenerFail(String errorMsg);
    }

    //Action is to identify different actions and implement different listeners
    public PostService(Context context, Object postListener, String action) {
        mContext = context;
        if(action.equals("addPost")) {
            mAddPostListener = (AddPostListener) postListener;
        } else if(action.equals("getAllPosts")) {
            mGetAllPostsListener = (GetAllPostsListener) postListener;
        } else if(action.equals("getSinglePost")) {
            mGetSinglePostListener = (GetSinglePostListener) postListener;
        } else if(action.equals("deletePost")) {
            mDeletePostListener = (DeletePostListener) postListener;
        }
    }

    //Create a new post
    public void addPost(Post post) {
        ParseObject parsePost = new ParseObject("Post");
        parsePost.put("content", post.getContent());
        parsePost.put("img", post.getImg());
        parsePost.put("createdBy", post.getCreatedBy());
        ArrayList<ParseObject> comments = new ArrayList<ParseObject>();
        parsePost.put("comments", comments);

        parsePost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                //Add post succeed.
                if (e == null) {
                    mAddPostListener.addPostSuccess();
                }
                //Add post failed.
                else {
                    mAddPostListener.addPostFail(e.getMessage().toString());
                }
            }
        });
    }

    //Get all posts
    public void getAllPosts(int limitNum) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        query.addDescendingOrder("createdAt");
        if(limitNum != 0)
            query.setLimit(limitNum);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> postlist, ParseException e) {
                if (e == null) {
                    List<Post> resultPostList = new ArrayList<Post>();
                    List<Post> tempList = new ArrayList<Post>();
                    for (int n = 0; n < postlist.size(); n++) {
                        Post currentPost = new Post();
                        currentPost.setObjectId(postlist.get(n).getObjectId());
                        currentPost.setCreatedAt(postlist.get(n).getCreatedAt());
                        currentPost.setUpdatedAt(postlist.get(n).getUpdatedAt());
                        currentPost.setCreatedBy(postlist.get(n).getParseUser("createdBy"));
                        currentPost.setContent(postlist.get(n).getString("content"));

                        //how to get the img array and assign it to the Post.mImg
                        ArrayList<ParseFile> tempImg = new ArrayList<ParseFile>();

                        try {
                            tempImg = (ArrayList<ParseFile>) postlist.get(n).fetchIfNeeded().get("img");
                        } catch (ParseException exception) {
                            exception.printStackTrace();
                        }
                        currentPost.setImg(tempImg);
                        tempList.add(currentPost);
                    }
                    resultPostList = tempList;

                    mGetAllPostsListener.getAllPostsSuccess(resultPostList);
                } else {
                    mGetAllPostsListener.getAllPostsFail(e.getMessage().toString());
                }
            }
        });

    }



    //Get a parsePost by postId
    public void getSinglePost(String postId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        query.include("createdBy");
        query.include("comments");
        query.getInBackground(postId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject post, ParseException e) {
                if (e == null) {
                    Post resultPost = new Post();
                    resultPost.setObjectId(post.getObjectId());
                    resultPost.setCreatedAt(post.getCreatedAt());
                    resultPost.setUpdatedAt(post.getUpdatedAt());
                    resultPost.setCreatedBy((ParseUser) post.get("createdBy"));
                    resultPost.setContent(post.get("content").toString());
                    ArrayList<ParseFile> postImg = new ArrayList<ParseFile>();
                    try {
                        postImg = (ArrayList<ParseFile>) post.fetchIfNeeded().get("img");
                    } catch (ParseException exception) {
                        exception.printStackTrace();
                    }
                    resultPost.setImg(postImg);
                    resultPost.setComments((ArrayList<ParseObject>) post.get("comments"));

                    mGetSinglePostListener.getSinglePostSuccess(resultPost);
                } else {
                    mGetSinglePostListener.getSinglePostFail(e.getMessage().toString());
                }
            }
        });
    }

    public void deletePost(String postId) {
        ParseObject menuObject = ParseObject.createWithoutData("Post", postId);
        menuObject.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    mDeletePostListener.deletePostListenerSuccess("Delete post successfully!");
                } else {
                    mDeletePostListener.deletePostListenerFail(e.getMessage().toString());
                }
            }
        });
    }
}
