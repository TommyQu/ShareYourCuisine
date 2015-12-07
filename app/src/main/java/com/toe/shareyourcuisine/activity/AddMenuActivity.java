package com.toe.shareyourcuisine.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.define.Define;
import com.toe.shareyourcuisine.R;
import com.toe.shareyourcuisine.adapter.ToeRecyclerAdapter;
import com.toe.shareyourcuisine.model.Menu;
import com.toe.shareyourcuisine.model.ToeRecyclerController;
import com.toe.shareyourcuisine.service.MenuService;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by TommyQu on 10/19/15.
 */
public class AddMenuActivity extends ActionBarActivity implements MenuService.AddMenuListener{

    private static final String TAG = "ToeNewMenuActivity";
    private  static final int RESULT_LOAD_IMAGE = 1;
    private EditText mMenuTitleValue;
    private ImageButton mMenuDisplayImgBtn;
    private EditText mMenuContentValue;
    private ImageButton mMenuImgBtn;
    private ImageView mDisplayImgView;
    private EditText mCookingTimeValue;
    private ArrayList<String> mDisplayImgPath = new ArrayList<>();
    private ArrayList<String> mImgPaths = new ArrayList<>();
    private Bitmap mDisPlayBitmap;
    private Button mSubmitBtn;
    private Button mCancelBtn;
    private int mMenuDisplayImgCount = 0;
    private int mMenuImgCount = 0;
    private String imgPickAction;

    private ImageView mMenuImgView;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ToeRecyclerAdapter mToeRecyclerAdapter;
    private ToeRecyclerController mToeRecyclerController;
    private ArrayList<ParseFile> mImgFiles = new ArrayList<ParseFile>();
    private ArrayList<Bitmap> mImgBitmaps = new ArrayList<Bitmap>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);
        getSupportActionBar().setTitle("New Menu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionBarRed)));
        mMenuTitleValue = (EditText)findViewById(R.id.menu_title_value);
        mMenuDisplayImgBtn = (ImageButton)findViewById(R.id.menu_display_img_button);
        mMenuContentValue = (EditText)findViewById(R.id.menu_content_value);
        mMenuImgBtn = (ImageButton)findViewById(R.id.menu_img_button);
        mDisplayImgView = (ImageView)findViewById(R.id.menu_display_img);
        mCookingTimeValue = (EditText)findViewById(R.id.menu_cooking_time_value);
        mSubmitBtn = (Button)findViewById(R.id.submit_btn);
        mCancelBtn = (Button)findViewById(R.id.cancel_btn);

        mMenuImgView = (ImageView)findViewById(R.id.menu_img);
        mRecyclerView = (RecyclerView)findViewById(R.id.menu_img_recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mToeRecyclerController = new ToeRecyclerController(this, mMenuImgView);
        mToeRecyclerAdapter = new ToeRecyclerAdapter(this, mToeRecyclerController, mImgPaths);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mToeRecyclerAdapter);

        mMenuDisplayImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgPickAction = "pickDisplayImg";
                FishBun.with(AddMenuActivity.this)
                        .setPickerCount(1)
                        .setActionBarColor(Color.parseColor("#FF3030"), Color.parseColor("#FF3030"))
                        .setArrayPaths(mDisplayImgPath)
                        .startAlbum();
            }
        });

        mMenuImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgPickAction = "pickImg";
                FishBun.with(AddMenuActivity.this)
                        .setPickerCount(10)
                        .setActionBarColor(Color.parseColor("#FF3030"), Color.parseColor("#FF3030"))
                        .setArrayPaths(mImgPaths)
                        .startAlbum();
            }
        });

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check() == true) {
                    Menu menu = new Menu();
                    menu.setmTitle(mMenuTitleValue.getText().toString());
                    menu.setmContent(mMenuContentValue.getText().toString());
                    menu.setmCookingTime(mCookingTimeValue.getText().toString());
                    //Decode display img
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    mDisPlayBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] displayImgByteArray = stream.toByteArray();
                    ParseFile displayImgFile = new ParseFile("displayImg.jpg", displayImgByteArray);
                    try {
                        displayImgFile.save();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    menu.setmDisplayImg(displayImgFile);

                    //Decode imgs
                    for(int i = 0; i < mImgBitmaps.size(); i++) {
                        ByteArrayOutputStream imgStream = new ByteArrayOutputStream();
                        mImgBitmaps.get(i).compress(Bitmap.CompressFormat.JPEG, 100, imgStream);
                        byte[] imgByteArray = imgStream.toByteArray();
                        ParseFile imgFile = new ParseFile("img.jpg",imgByteArray);
                        try {
                            imgFile.save();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        mImgFiles.add(imgFile);
                    }
                    menu.setmImg(mImgFiles);
                    MenuService menuService = new MenuService(AddMenuActivity.this, AddMenuActivity.this, "addMenu");
                    menuService.addMenu(menu);
                }
            }
        });
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Define.ALBUM_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    if(imgPickAction.equals("pickDisplayImg")) {
                        mDisplayImgPath = data.getStringArrayListExtra(Define.INTENT_PATH);
                        Glide.with(AddMenuActivity.this).load(mDisplayImgPath.get(0)).fitCenter().into(mDisplayImgView);
                        break;
                    }
                    else if(imgPickAction.equals("pickImg")) {
                        mImgPaths = data.getStringArrayListExtra(Define.INTENT_PATH);
                        mToeRecyclerAdapter.changePath(mImgPaths);
                        break;
                    }
                }
        }
    }

    private boolean check() {
        if(mMenuTitleValue.getText().toString().equals("")
                || mMenuContentValue.getText().toString().equals("")
                || mCookingTimeValue.getText().toString().equals("")) {
            Toast.makeText(AddMenuActivity.this, "Please fill required text fields!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(mDisplayImgView.getDrawable() == null) {
            Toast.makeText(AddMenuActivity.this, "Please upload a display image!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            mDisPlayBitmap = BitmapFactory.decodeFile(mDisplayImgPath.get(0));

            //Compress img to meet the ParseFile maximum requirements
            if(mDisPlayBitmap.getByteCount() >= 10485760)
                mDisPlayBitmap = Bitmap.createScaledBitmap(mDisPlayBitmap, 1920, 1080, true);
            for(int i = 0;i < mToeRecyclerAdapter.getItemCount(); i++) {
                Bitmap currentImg = BitmapFactory.decodeFile(mImgPaths.get(i));
                if(currentImg.getByteCount() >= 10485760)
                    currentImg = Bitmap.createScaledBitmap(currentImg, 1920, 1080, true);
                mImgBitmaps.add(currentImg);
            }
            return true;
        }
    }

    @Override
    public void addMenuSuccess() {
        Toast.makeText(AddMenuActivity.this, "Add menu successfully!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(AddMenuActivity.this, MenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }

    @Override
    public void addMenuFail(String errorMsg) {
        Toast.makeText(AddMenuActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
    }
}
