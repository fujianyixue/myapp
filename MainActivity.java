package com.mycompany.myapp;

import android.app.*;
import android.content.*;
import android.os.*;
import android.text.*;
import android.view.*;
import android.webkit.*;
import android.widget.*;
import android.widget.AdapterView.*;
import org.json.*;

public class MainActivity extends Activity
{
	obj o;
	WebView webView;
	int x1,x2,index;
	file1 f;
    LinearLayout lin1,lin2,lin3,sub_;
	LinearLayout[] lin;
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		ini_webview();
		ini_layout();
		index=0;

		webView.loadUrl("file:///storage/sdcard0/MyApp001/index.html"); 
		Button btest=new Button(this);
		f=new file1(this.getString(R.string.app_name));
	}
	
	public void button(View view)
	{
		Button b=(Button)view;
		EditText file_name=(EditText)findViewById(R.id.file_name);
		String sname=file_name.getText().toString();
		//alert(b.getText().toString());
		switch(b.getId()){
			case R.id.back:
				if(webView.canGoBack()){webView.goBack();}
			break;
			case R.id.forward:
				if(webView.canGoForward()){webView.goForward();}
				break;
			case R.id.set:
				show(lin[0]);
				break;
			case R.id.index:
				show(lin[1]);
				break;
			case R.id.home:
				show(lin[2]);
				break;
			case R.id.url:
				EditText e=(EditText)findViewById(R.id.edit1);
				String s=e.getText().toString();
				webView.loadUrl(s);
				break;
			case R.id.createfile:
				if(sname==""){break;}
				if(f.isSdcardAvailable())
				{
					if(f.createFile(sname)){alert("文件超级成功");}
				}
				break;
			case R.id.createfolde:
				if(!(sname=="")){
				f.createFolder(sname);
				}
				break;
			case R.id.write:
				f.write("test.txt",sname,true);
				break;
			case R.id.read:
				ini(sname);
				//alert("rrr");
				//alert(f.read(path,"test.txt"));
				break;
			case R.id.test:
				alert(f.path());
						webView.loadUrl("file:///storage/sdcard0/MyApp001/demo.html"); 
				show(lin[0]);
				break;
			case R.id.test2:

						ListView c=(ListView)findViewById(3);
				webView.loadUrl("file:///storage/sdcard0/1.html");
				//alert(webView.getContext().toString());
		}
	}
	
		@Override
		public boolean onTouchEvent(MotionEvent event) {
				final int x = (int)event.getX();
				
				EditText text=(EditText)findViewById(R.id.file_name);
				//text.setText(Integer.toString(x));
				switch(event.getAction()) {
						case MotionEvent.ACTION_DOWN:
								x1=(int)event.getX();
								text.setText(Integer.toString(x)+"down");
								o=new obj(sub_);
								break;
						case MotionEvent.ACTION_MOVE:
								//text.setText(Integer.toString(x)+"move");

								x2=(int)event.getX();
								o.set(x2-x1);
								break;
						case MotionEvent.ACTION_UP:
								o=null;
								//text.setText(Integer.toString(x)+"up");
								if(x2-x1>100&&index!=0){index=index-1;}
								else
								if(x2-x1<-100&&index!=2){index++;}
								show(lin[index]);
								break;
				}
				return true;
		}
	
	public void yourMethodName(View view)
	{
		//EditText e=(EditText) findViewById(R.id.edit1);
		//e.setText("rrrr");
		View v=getWindow().getDecorView();
		//webView.loadUrl("http://fuyida.sinaapp.com/");
		//Toast.makeText(MainActivity.this,"test",Toast.LENGTH_SHORT).show();
		file2 f=new file2();
		f.t(v,MainActivity.this);	
	}
	
	public void show(LinearLayout now_)
	{
		sub_.setVisibility(View.GONE);
	  now_.setVisibility(View.VISIBLE);
		sub_=now_;
	}
	
	public void ini(String js){
		String s=f.read("ini.txt");
		
		JSONObject json;
		try{
			json=new JSONObject(s);
			alert("");
		    if(json.isNull("set")){
				json.put("set","setme");
				alert(js);
			}else{json.put("set",js);}
		    alert(json.toString());
		    //json.remove("a");
		
			f.write("ini.txt",json.toString(),false);
		
		}catch(Exception e){
			alert("falsejs");
		}
	}
	public void ini_layout()
	{
		lin=new LinearLayout[3];
		lin[0] = (LinearLayout) findViewById(R.id.main1);
		lin[1] = (LinearLayout) findViewById(R.id.main2);
		lin[2] = (LinearLayout) findViewById(R.id.main3);
		sub_ = lin[0];
		ListView b=new ListView(this);
			String[] companies = new String[] { "Google", "Apple", "Facebook",
        	"Blackberry", "Samsung", "Twitter", "Intel", "HTC", "Asus" };

			ListAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, companies);
		
			b.setAdapter(adapter);
			b.setAddStatesFromChildren(true);
			b.setId(3);
			b.setOnItemClickListener(new OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> l, View v, int position, long id)
							{
									String s=(String)l.getItemAtPosition(position);
									Toast.makeText(MainActivity.this,s,Toast.LENGTH_LONG).show();
							}
					});
		lin[2].addView(b);
	}
	public void ini_webview()
	{
		webView =(WebView) findViewById(R.id.webView); 
		WebSettings settings =webView.getSettings();
		settings.setJavaScriptEnabled(true);
		//settings.setDefaultFontSize(35);
		settings.setBuiltInZoomControls(true);
		settings.setUseWideViewPort(true); 
		settings.setLoadWithOverviewMode(true);
		settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		//settings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		webView.addJavascriptInterface(new file1(), "file1");
		webView.addJavascriptInterface(new fso(MainActivity.this), "fso");
		webView.setWebChromeClient(new WebChromeClient() {
				@Override
				public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result)
				{
					return false;
				}
			});
		webView.setWebViewClient (new WebViewClient() {
                public boolean shouldOverrideUrlLoading (WebView view, String url) 
				{
					webView.loadUrl(url);
					return true ; 
				}
			});
	}
	

	public void alert(String s)
	{
		Toast.makeText(MainActivity.this,s,Toast.LENGTH_SHORT).show();
	}
	public void alert(int i)
	{
		String s=Integer.toString(i);
		Toast.makeText(MainActivity.this,s,Toast.LENGTH_SHORT).show();
	}
}

class file2
{
	Layout l;
	
	public void t(View t,Context c){
		
		EditText e=(EditText) t.findViewById(R.id.edit1);
		e.setText("rrrryyy");
		Toast.makeText(c,"",Toast.LENGTH_SHORT).show();
	}
	int y=2;
	
}

class obj
{
    LinearLayout now_;int x1,x2,index,l,t,w,h;
		
    obj(LinearLayout now_){
				this.now_=now_;
				l= now_.getLeft();
		t= now_.getTop();
		w = now_.getWidth();
		h = now_.getHeight();
	}
	
	public void set(int l1){
	this.l=this.l +l1;
	now_.layout(l, t, l+w, t+h);
	}
	public void show(){
	}
}
