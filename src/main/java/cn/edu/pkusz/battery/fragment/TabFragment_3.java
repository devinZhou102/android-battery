package cn.edu.pkusz.battery.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import cn.edu.pkusz.battery.R;
import cn.edu.pkusz.battery.activity.TabFragment_3_1;
import cn.edu.pkusz.battery.activity.TabFragment_3_2;
import cn.edu.pkusz.battery.activity.TabFragment_3_3;



/**
 * 
 * xin
 * 
 */
public class TabFragment_3 extends Fragment 
{
	Button XinButton1, XinButton2, XinButton3;
	ImageButton XinButton10;
	private ListView myList; // ListView控件 
	private AddNumberBaseAdpater addNumberBaseAdpater;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getActivity().getActionBar().setTitle(R.string.title_model);
		
		View view = inflater.inflate(R.layout.tab_fragment_3, container, false);
		XinButton1 = (Button)view.findViewById(R.id.XinButton1);
		XinButton1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
					Intent intent = new Intent(getActivity(), TabFragment_3_1.class);
				    startActivity(intent);
			}
		});
		
		XinButton2 = (Button)view.findViewById(R.id.XinButton2);
		XinButton2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
					Intent intent = new Intent(getActivity(), TabFragment_3_2.class);
				    startActivity(intent);
			}
		});
		
		
		addNumberBaseAdpater=new AddNumberBaseAdpater(getActivity().getApplicationContext());
		myList = (ListView) view.findViewById(R.id.listview1);
		myList.setAdapter(addNumberBaseAdpater);
		
		
		return view;
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (this.isVisible()) {
			getActivity().getActionBar().setTitle(R.string.title_model);
		} 
	}
	
	
	public class AddNumberBaseAdpater extends BaseAdapter {
		private LayoutInflater mInflater;
		private ArrayList<String> text;
		int i=1;

		public AddNumberBaseAdpater(Context context) {
			text = new ArrayList<String>();
			text.add("mode" + i);// 加载第一项
			this.mInflater = LayoutInflater.from(context);
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return text.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return text.get(position);
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = new ViewHolder();
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.addnumber, null);
				holder.btnOpen = (ImageButton) convertView
						.findViewById(R.id.ibtnAddNumber);
				holder.editText = (Button) convertView
						.findViewById(R.id.editNumber);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			switch (position) {// 不要以为XML文件中是2个按钮，其实只有一个
			case 0:
				holder.btnOpen.setBackgroundResource(R.drawable.add1);// 第一项按钮则显示加号图片
				holder.editText.setText("自定义模式"+(position+1));// 第一项
				break;

			default:
				holder.btnOpen.setBackgroundResource(R.drawable.deletenumber);// 超过了一项则显示减号图片，可以删除
				holder.editText.setText("自定义模式"+(position+1));// 第一项
				break;
			}
			// Button XinAddButton = (Button)
			// convertView.findViewById(R.id.XinAddButton);
			holder.btnOpen.setOnClickListener(new View.OnClickListener() {// 添加按钮
				public void onClick(View v) {
					if (position == 0) {
						text.add("mode" + (++i));// 添加一项控件

					} else if (position > 0) {// 始终留一项不能删除

						text.remove(text.size() - 1);// 删除按钮
						i--;
					}
					notifyDataSetChanged();
				}
			});


			holder.editText.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Log.i("lin1", text.get(position));
					Intent intent = new Intent(getActivity(), TabFragment_3_3.class);
					intent.putExtra("mode", text.get(position));
				    startActivity(intent);
				}
			});

			return convertView;
		}

		public final class ViewHolder {
			public Button editText;
			public ImageButton btnOpen;

		}
	}	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
