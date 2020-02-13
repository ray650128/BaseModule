package tw.com.lig.module_base.base;


import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;


public  class InstantToast {
	private static Toast mToast = null; 
	private static Toast imageToast=null;
	 public static void show(Context context,String ss){
		 
		 if( null == context || TextUtils.isEmpty( ss ) ){  
	           return;  
	       }  
	         
	       if( null == mToast ){  
	           mToast = Toast.makeText( context, ss, Toast.LENGTH_SHORT);  
	       }else{  
	           mToast.setText( ss );  
	       } 
	         
	          // mToast.setDuration(500);
			   mToast.setGravity(Gravity.CENTER, 0, 0);
			  // LinearLayout toastView = (LinearLayout) mToast.getView();
			/*   ImageView imageCodeProject = new ImageView(getApplicationContext());
			   imageCodeProject.setImageResource(R.drawable.icon);
			   toastView.addView(imageCodeProject, 0);*/
			   mToast.show();
	 }




	

	 
}
