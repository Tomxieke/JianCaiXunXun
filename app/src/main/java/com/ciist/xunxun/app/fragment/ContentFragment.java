package com.ciist.xunxun.app.fragment;

import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.ciist.common.Common;
import com.ciist.entites.CiistObjects;
import com.ciist.xunxun.R;
import com.ciist.xunxun.app.adapter.CiistListAdapter;
import com.ciist.xunxun.app.util.HttpUtil;
import com.ciist.xunxun.app.widget.PullUpListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ContentFragment extends Fragment {

	private static final int PAGE_SIZE = 10;
	private static final String ARG_POSITION = "0x45F0000";

	private PullUpListView xunxun_content_lv;
	private ProgressBar xunxuncontent_pb;
	private String LinkCode;
	private CiistListAdapter adapter;
	private List<CiistObjects> mdata = new ArrayList<>();
	private List<CiistObjects> contentdata = new ArrayList<>();
	private int page = 1;

	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			//设置适配器
			contentdata.addAll(mdata);
			if (adapter == null){
				adapter = new CiistListAdapter(getActivity());
				adapter.setData(contentdata);
				xunxun_content_lv.setAdapter(adapter);
			}else {
				if (mdata.size() <= 0) {
					//Toast.makeText(getActivity(), "没有咯，亲", Toast.LENGTH_SHORT).show();
					xunxun_content_lv.setFooterVisibily(false);
				} else {
					adapter.notifyDataSetChanged();
				}
			}
			xunxun_content_lv.setVisibility(View.VISIBLE);
			xunxuncontent_pb.setVisibility(View.GONE);
		}
	};


	public static ContentFragment newInstance(String linkCode) {
		ContentFragment f = new ContentFragment();
		Bundle b = new Bundle();
		b.putString(ARG_POSITION, linkCode);
		f.setArguments(b);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinkCode = getArguments().getString(ARG_POSITION);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_xunxun_content,null);
		xunxun_content_lv = (PullUpListView) view.findViewById(R.id.xunxun_content_lv);
		xunxuncontent_pb = (ProgressBar) view.findViewById(R.id.xunxuncontent_pb);
		getData(LinkCode, page, PAGE_SIZE);
		reListView();
		return view;
	}

	/**listView刷新**/
	private void reListView() {
		xunxun_content_lv.setOnPullUpListViewCallBack(new PullUpListView.OnPullUpListViewCallBack() {
			@Override
			public void scrollBottomState() {
				page++;
				getData(LinkCode,page,PAGE_SIZE);
			}
		});
	}

	/**
	 *
	 * @param c code
	 * @param page  页数
	 * @param size  条数
	 */
	private void getData(final String c, final int page, int size){
		if (Common.getNetState(getContext()) && HttpUtil.isCacheDataFailure(getContext(),c + page)) {
			try {
				String strUTF8 = URLEncoder.encode(c, "UTF-8");
				final String url = "http://www.ciist.com:2015/chinabmserver/xx/getdata/ciistkey/" + strUTF8 + "/" + page + "/" + size;
				new Thread(new Runnable() {
					@Override
					public void run() {
						String s = Common.fromNetgetData(url);
						if (page == 1) {
							HttpUtil.saveObject(getContext(), s, c + page);
						}
						try {
							List<CiistObjects> data = new ArrayList<>();
							JSONArray array = new JSONArray(s);
							for (int i = 0; i < array.length(); i++) {
								CiistObjects o = new CiistObjects();
								JSONObject object = array.getJSONObject(i);
								o.setAbs(object.getString("Abs"));
								o.setAuthor(object.getString("Author"));
								o.setAuthorFlag(object.getString("AuthorFlag"));
								o.setBannerbuttonFlag(object.getString("BannerbuttonFlag"));
								o.setBannerbuttonUrl(object.getString("BannerbuttonUrl"));
								o.setBannerbuttontitle(object.getString("Bannerbuttontitle"));
								o.setBannertitle(object.getString("Bannertitle"));
								o.setBannertitleColor(object.getString("BannertitleColor"));
								o.setByuids(object.getString("Byuids"));
								o.setContents(object.getString("Contents"));
								o.setGuids(object.getString("Guids"));
								o.setImageFlag(object.getString("ImageFlag"));
								o.setImageHeight(object.getString("ImageHeight"));
								o.setImageLabels(object.getString("ImageLabels"));
								o.setImageLabelsFlag(object.getString("ImageLabelsFlag"));
								o.setImagePath(object.getString("ImagePath"));
								o.setImageSearch(object.getString("ImageSearch"));
								o.setIsort(object.getString("Isort"));
								o.setKeywords(object.getString("Keywords"));
								o.setLinkCode(object.getString("LinkCode"));
								o.setLinkModel(object.getString("LinkModel"));
								o.setLongitue(object.getString("Longitue"));
								o.setModeType(object.getString("ModeType"));
								o.setPubdataFlag(object.getString("PubdataFlag"));
								o.setSelfids(object.getString("Selfids"));
								o.setShortTitle(object.getString("ShortTitle"));
								o.setShortTitleFlag(object.getString("ShortTitleFlag"));
								o.setSourcefrom(object.getString("Sourcefrom"));
								o.setSourcefromFlag(object.getString("SourcefromFlag"));
								o.setSubTitleFlag(object.getString("SubTitleFlag"));
								o.setSubtitle(object.getString("Subtitle"));
								o.setTatidue(object.getString("Tatidue"));
								o.setTimestamp(object.getString("Timestamp"));
								o.setTitle(object.getString("Title"));
								o.setTitleFlag(object.getString("TitleFlag"));
								o.setVisible(object.getString("Visible"));
								o.setVisitCount(object.getString("VisitCount"));
								o.setVisitCountFlag(object.getString("VisitCountFlag"));
								o.setShareLink(object.getString("ShareLink"));
								o.setTel(object.getString("Tel"));
								data.add(o);
							}
							mdata.clear();
							mdata = data;
							mHandler.sendMessage(new Message());
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}).start();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}else {    //无网络情况下读取文件中的数据
			String s = HttpUtil.readObject(getContext(),c+page);
			try {
				List<CiistObjects> data = new ArrayList<>();
				JSONArray array = new JSONArray(s);
				for (int i = 0; i < array.length(); i++) {
					CiistObjects o = new CiistObjects();
					JSONObject object = array.getJSONObject(i);
					o.setAbs(object.getString("Abs"));
					o.setAuthor(object.getString("Author"));
					o.setAuthorFlag(object.getString("AuthorFlag"));
					o.setBannerbuttonFlag(object.getString("BannerbuttonFlag"));
					o.setBannerbuttonUrl(object.getString("BannerbuttonUrl"));
					o.setBannerbuttontitle(object.getString("Bannerbuttontitle"));
					o.setBannertitle(object.getString("Bannertitle"));
					o.setBannertitleColor(object.getString("BannertitleColor"));
					o.setByuids(object.getString("Byuids"));
					o.setContents(object.getString("Contents"));
					o.setGuids(object.getString("Guids"));
					o.setImageFlag(object.getString("ImageFlag"));
					o.setImageHeight(object.getString("ImageHeight"));
					o.setImageLabels(object.getString("ImageLabels"));
					o.setImageLabelsFlag(object.getString("ImageLabelsFlag"));
					o.setImagePath(object.getString("ImagePath"));
					o.setImageSearch(object.getString("ImageSearch"));
					o.setIsort(object.getString("Isort"));
					o.setKeywords(object.getString("Keywords"));
					o.setLinkCode(object.getString("LinkCode"));
					o.setLinkModel(object.getString("LinkModel"));
					o.setLongitue(object.getString("Longitue"));
					o.setModeType(object.getString("ModeType"));
					o.setPubdataFlag(object.getString("PubdataFlag"));
					o.setSelfids(object.getString("Selfids"));
					o.setShortTitle(object.getString("ShortTitle"));
					o.setShortTitleFlag(object.getString("ShortTitleFlag"));
					o.setSourcefrom(object.getString("Sourcefrom"));
					o.setSourcefromFlag(object.getString("SourcefromFlag"));
					o.setSubTitleFlag(object.getString("SubTitleFlag"));
					o.setSubtitle(object.getString("Subtitle"));
					o.setTatidue(object.getString("Tatidue"));
					o.setTimestamp(object.getString("Timestamp"));
					o.setTitle(object.getString("Title"));
					o.setTitleFlag(object.getString("TitleFlag"));
					o.setVisible(object.getString("Visible"));
					o.setVisitCount(object.getString("VisitCount"));
					o.setVisitCountFlag(object.getString("VisitCountFlag"));
					o.setShareLink(object.getString("ShareLink"));
					o.setTel(object.getString("Tel"));
					data.add(o);
				}
				mdata.clear();
				mdata = data;
				mHandler.sendMessage(new Message());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

}