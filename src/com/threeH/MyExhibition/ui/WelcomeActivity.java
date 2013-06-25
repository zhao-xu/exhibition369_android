package com.threeH.MyExhibition.ui;

import android.content.Intent;
import android.os.Bundle;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.cache.XmlDB;
import com.threeH.MyExhibition.common.StringPools;
import com.threeH.MyExhibition.service.ClientController;
import com.threeH.MyExhibition.tools.MobileConfig;


public class WelcomeActivity extends BaseActivity implements ActivityInterface{
	private ClientController controller;
	private String mJsonData;
    private String mOsVer;
    private String mVer;
    private String mMacAddress;
    private MobileConfig mMobileConfig;

    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentViewWithNoTitle(R.layout.welcom_page);

		controller = ClientController.getController(this);
        mMobileConfig = MobileConfig.getMobileConfig(context);
		initdata();

		new Thread() {
				public void run() {
//                    mProDialog.show();
				    try {
						/*mJsonData = controller.getService().OverAllData(StringPools.PHONE_TYPE,mOsVer,mVer,mMacAddress);

						XmlDB.getInstance(WelcomeActivity.this).saveKey(
                                StringPools.OVERALL_CONFIG, mJsonData);*/
                        MobileConfig mobileConfig = MobileConfig.getMobileConfig(context);
                        XmlDB.getInstance(WelcomeActivity.this).saveKey(StringPools.OVERALL_CONFIG,
                                mController.getService().OverAllData(StringPools.PHONE_TYPE,
                                        "369exhibition", "1.0", mobileConfig.getLocalMacAddress()));
                        sleep(3000);
                        goToNextPage();

				} catch (InterruptedException e) {
					    e.printStackTrace();
                        mProDialog.dismiss();
				} catch (Exception e) {   
					    e.printStackTrace();
                        mProDialog.dismiss();
				} 
			};
		}.start();      

	}

	@Override
	public void findView() {

	}

    @Override
    public void initdata() {
        mOsVer = mMobileConfig.getMobileOsVersion();
        mVer = mMobileConfig.getPkgVerName();
        mMacAddress = mMobileConfig.getLocalMacAddress();
    }

    @Override
	public void addAction() {   

	}

	private void goToNextPage() {   
		Intent intent = new Intent(WelcomeActivity.this, HomeOfTabActivity.class);
		startActivity(intent);
        mProDialog.dismiss();
		finish();
	}
	
}
