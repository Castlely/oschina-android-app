package net.oschina.app.fragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.oschina.app.AppContext;
import net.oschina.app.AppData;
import net.oschina.app.bean.Information;
import net.oschina.app.bean.InformationList;
import net.oschina.app.ui.BackHandledFragment;
import net.oschina.app.ui.MainActivity;
import net.oschina.designapp.R;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.sqk.viewpager.widget.CircleFlowIndicator;
import org.sqk.viewpager.widget.ViewFlow;

import android.app.ActionBar;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sqk.GridView.Grid_Item;
import com.sqk.GridView.MyGridAdaper;
import com.sqk.viewpager.ImageAdapter;

public class IndexFragment extends BackHandledFragment {
    private ViewFlow  viewFlow;
    private FinalHttp fh = new FinalHttp();
    GridView          MyGridView;
    List<Grid_Item>   lists;
    private ActionBar mActionBar;
    private FinalHttp finalHttp;
    View              viewTitleBar;
    ImageView         weatherImageView;
    TextView          weatherTextView;
    private byte[]    weatherPicByte;
    ImageView         rightBtn;

    public static Drawable resizeImage(Bitmap bitmap, int w, int h) {
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // if you want to rotate the Bitmap     
        // matrix.postRotate(45);     
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width, height, matrix, true);
        return new BitmapDrawable(resizedBitmap);
    }

    private void initActionBar(LayoutInflater inflater, String titleString) {
        mActionBar = this.getActivity().getActionBar();

        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);

        TextView title = (TextView) viewTitleBar.findViewById(R.id.action_bar_title);
        title.setText(titleString);
        rightBtn = (ImageView) viewTitleBar.findViewById(R.id.right_btn);
        finalHttp.get(AppData.WEATHERAPI, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String t) {
                Map<String, Object> weather = AppData.gsonBuilder.create().fromJson(t,
                    new TypeToken<Map<String, Object>>() {
                    }.getType());
                Map<String, Object> dataMap = (Map<String, Object>) ((List) ((Map<String, Object>) ((List) weather
                    .get("results")).get(0)).get("weather_data")).get(0);
                final String imgUrl = (String) dataMap.get("dayPictureUrl");
                String text = (String) dataMap.get("temperature");

                final Handler handle = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        if (msg.what == 1) {
                            if (weatherPicByte != null) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(weatherPicByte, 0,
                                    weatherPicByte.length);
                                Bitmap bitmap2 = transparentImage(bitmap);
                                weatherImageView.setBackground(resizeImage(bitmap2,
                                    rightBtn.getWidth() + 10, rightBtn.getHeight() + 10));
                                //bitmapManager.loadBitmap("http://i.tq121.com.cn/i/wap/80bai/d01.png", weatherImageView);
                            }
                        }
                    }
                };

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL("http://i.tq121.com.cn/i/wap/80bai/d01.png");
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.setReadTimeout(10000);

                            if (conn.getResponseCode() == 200) {
                                InputStream fis = conn.getInputStream();
                                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                byte[] bytes = new byte[1024];
                                int length = -1;
                                while ((length = fis.read(bytes)) != -1) {
                                    bos.write(bytes, 0, length);
                                }
                                weatherPicByte = bos.toByteArray();
                                bos.close();
                                fis.close();

                                Message message = new Message();
                                message.what = 1;
                                handle.sendMessage(message);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                new Thread(runnable).start();
                weatherTextView.setText(text);
            }
        });
        mActionBar.setCustomView(viewTitleBar, lp);
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionBar.setDisplayShowCustomEnabled(true);

    }

    public static Bitmap drawableToBitmap(Drawable drawable) {

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable
            .getIntrinsicHeight(),
            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public Bitmap transparentImage(Bitmap bmp) {
        int m_ImageWidth, m_ImageHeigth;
        m_ImageWidth = bmp.getWidth();
        m_ImageHeigth = bmp.getHeight();
        int[] m_BmpPixel = new int[m_ImageWidth * m_ImageHeigth];
        bmp.getPixels(m_BmpPixel, 0, m_ImageWidth, 0, 0, m_ImageWidth, m_ImageHeigth);
        int pin = m_BmpPixel[0];
        for (int i = 0; i < m_ImageWidth * m_ImageHeigth; i++) {
            if (m_BmpPixel[i] == pin) {
                m_BmpPixel[i] = 0x001156E1;
            }
        }
        Bitmap resultBitmap = Bitmap.createBitmap(m_BmpPixel, m_ImageWidth, m_ImageHeigth,
            Config.ARGB_8888);
        return resultBitmap;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_index, container, false);

        finalHttp = new FinalHttp();
        viewTitleBar = inflater.inflate(R.layout.action_bar_index, null);
        weatherImageView = (ImageView) viewTitleBar.findViewById(R.id.weather);
        weatherTextView = (TextView) viewTitleBar.findViewById(R.id.weather_text);
        String url = "http://www.psxq.gov.cn/app/opendata/getData/65170?pageSize=3&pageNo=1";
        fh.get(url, new AjaxCallBack<String>() {

            @Override
            public void onSuccess(String resultString) {
                Gson gson = AppData.gsonBuilder.create();
                InformationList informationList = gson
                    .fromJson(resultString, InformationList.class);
                Map<String, Information> imgMap = Maps.newHashMap();
                for (int n = 0; n < 3; n++) {
                    AppData.set("information_" + informationList.getDataList().get(n).getId(), gson
                        .toJson(informationList.getDataList().get(n)), IndexFragment.this
                        .getActivity().getApplication());
                    String img = informationList
                        .getDataList()
                        .get(n)
                        .getDescription()
                        .substring(
                            informationList.getDataList().get(n).getDescription()
                                .indexOf("/uploadfiles/"));
                    img = img.substring(0, img.indexOf("\">"));
                    img = "http://www.psxq.gov.cn" + img;
                    imgMap.put(img, informationList.getDataList().get(n));
                }
                viewFlow.setAdapter(new ImageAdapter(IndexFragment.this.getActivity(), imgMap));
            }

        });
        viewFlow = (ViewFlow) view.findViewById(R.id.viewflow);
        viewFlow.setmSideBuffer(3); // 实际图片张数， 我的ImageAdapter实际图片张数为3
        CircleFlowIndicator indic = (CircleFlowIndicator) view.findViewById(R.id.viewflowindic);
        viewFlow.setFlowIndicator(indic);
        viewFlow.setTimeSpan(4500);
        viewFlow.setSelection(3 * 1000); // 设置初始位置
        viewFlow.startAutoFlowTimer(); // 启动自动播放
        //
        MyGridView = (GridView) view.findViewById(R.id.main_grid);

        lists = new ArrayList<Grid_Item>();
        String isShortCutString = AppData.get("isShortCut", (AppContext) this.getActivity()
            .getApplication());
        if (isShortCutString.length() != 0)
            AppData.isShortCut = AppData.gsonBuilder.create().fromJson(isShortCutString,
                new TypeToken<Map<String, Boolean>>() {
                }.getType());
        for (Entry<String, Boolean> entry : AppData.isShortCut.entrySet()) {
            if (entry.getValue() == true)
                lists.add(new Grid_Item(AppData.ico.get(entry.getKey()), entry.getKey()));
        }
        MyGridAdaper adaper = new MyGridAdaper(IndexFragment.this.getActivity(), lists,
            this.getFragmentManager());
        MyGridView.setAdapter(adaper);
        initActionBar(inflater, "坪山新区政府在线");
        view.post(new Runnable() {

            @Override
            public void run() {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.setFootBarButtonState(1, false);
                mainActivity.setFootBarButtonState(2, false);
                mainActivity.setFootBarButtonState(3, false);
                mainActivity.setFootBarButtonState(4, false);
            }
        });
        return view;
    }

    @Override
    protected boolean onBackPressed() {
        return true;
    }
}
