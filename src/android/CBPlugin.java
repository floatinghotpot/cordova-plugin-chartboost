package com.rjfun.cordova.chartboost;

import org.json.JSONObject;

import android.util.Log;
import android.view.View;

import com.chartboost.sdk.*;
import com.chartboost.sdk.Model.CBError.CBImpressionError;
import com.rjfun.cordova.ad.GenericAdPlugin;

public class CBPlugin extends GenericAdPlugin {
    private static final String LOGTAG = "Chartboost";
    
    // options
    private static final String OPT_APP_ID = "appId";
    private static final String OPT_APP_KEY = "appKey";

    private static final String TEST_BANNER_ID = "banner/not_supported";
    private static final String TEST_INTERSTITIAL_ID = "interstitial/Home Screen";
    
    private static final String TEST_APPID = "543f9b02c26ee436f622d806";
    private static final String TEST_APPKEY = "5091e071a0e429129ea7dc5b69fe005296de40ef";
    
    private String mAppId = TEST_APPID;
    private String mAppKey = TEST_APPKEY;
    
    private boolean mAppIdSet = false;

    @Override
    protected void pluginInitialize() {
    	super.pluginInitialize();
    	
    	// TODO: any init code
        Chartboost.startWithAppId(getActivity(), mAppId, mAppKey);
	}

	@Override
	protected String __getProductShortName() {
		return "Chartboost";
	}
	
	@Override
	protected String __getTestBannerId() {
		return TEST_BANNER_ID;
	}

	@Override
	protected String __getTestInterstitialId() {
		return TEST_INTERSTITIAL_ID;
	}

	@Override
	public void setOptions(JSONObject options) {
		Log.d(LOGTAG, "setOptions");
		super.setOptions(options);
		
		if(options.has(OPT_APP_ID)) this.mAppId = options.optString(OPT_APP_ID);
		if(options.has(OPT_APP_KEY)) this.mAppKey = options.optString(OPT_APP_KEY);
	}
	
	@Override
	protected View __createAdView(String adId) {
		return this.getView();
	}
	
	@Override
	protected void __loadAdView(View view) {
	}
	
	@Override
	protected int __getAdViewWidth(View view) {
		return view.getWidth();
	}

	@Override
	protected int __getAdViewHeight(View view) {
		return 0;
	}

	@Override
	protected void __pauseAdView(View view) {
	}

	@Override
	protected void __resumeAdView(View view) {
	}

	@Override
	protected void __destroyAdView(View view) {
	}
	
	protected void validateChartboost() {
		if(! mAppIdSet) {
			Log.d(LOGTAG, "startWithAppId: " + mAppId + ", " + mAppKey);
			Chartboost.startWithAppId(getActivity(), mAppId, mAppKey);
			Chartboost.setDelegate(new CBAdEventDelegate());
			Chartboost.onCreate(getActivity());
			Chartboost.onStart(getActivity());
			mAppIdSet = true;
		}
	}
	
	@Override
	protected Object __createInterstitial(String adId) {
		Log.d(LOGTAG, "__createInterstitial");
		validateChartboost();
		
		String[] items = adId.split("/");
		if(items.length >= 2) {
			return adId;
		}
        return null;
	}
	
	@Override
	protected void __loadInterstitial(Object interstitial) {
		Log.d(LOGTAG, "__loadInterstitial");
		if(interstitial == null) return;
		
		if(interstitial instanceof String) {
			String[] items = ((String) interstitial).split("/");
			if(items.length >= 2) {
				String adType = items[0];
				String adLocation = items[1];
				
				if("video".compareTo(adType) == 0) {
					Chartboost.cacheRewardedVideo(adLocation);
					
				} else if("moreapps".compareTo(adType) == 0) {
					Chartboost.cacheMoreApps(adLocation);
					
				} else {
					Chartboost.cacheInterstitial(adLocation);
				}
			}
		}
	}
	
	@Override
	protected void __showInterstitial(Object interstitial) {
		Log.d(LOGTAG, "__showInterstitial");
		if(interstitial == null) return;
		
		if(interstitial instanceof String) {
			String[] items = ((String) interstitial).split("/");
			if(items.length >= 2) {
				String adType = items[0];
				String adLocation = items[1];
				
				if("video".compareTo(adType) == 0) {
					if(Chartboost.hasRewardedVideo(adLocation)) Chartboost.showRewardedVideo(adLocation);
					
				} else if("moreapps".compareTo(adType) == 0) {
					if(Chartboost.hasMoreApps(adLocation)) Chartboost.showMoreApps(adLocation);
					
				} else {
					if(Chartboost.hasInterstitial(adLocation)) Chartboost.showInterstitial(adLocation);
				}
			}
		}
	}

	@Override
	protected void __destroyInterstitial(Object interstitial) {
		Log.d(LOGTAG, "__destroyInterstitial");
		if(interstitial == null) return;

		// no need do anything
	}

    @Override
    public void onPause(boolean multitasking) {
    	Chartboost.onPause(getActivity());
        super.onPause(multitasking);
    }
    
    @Override
    public void onResume(boolean multitasking) {
    	super.onResume(multitasking);
    	Chartboost.onResume(getActivity());
    }

    @Override 
    public void onStart() {
    	super.onStart();
        Chartboost.onStart(getActivity());
    }
    
    @Override
    public void onDestroy() {
    	Chartboost.onDestroy(getActivity());
        super.onDestroy();
    }
    
    /**
     * document.addEventListener('onAdLoaded', function(data));
     * document.addEventListener('onAdFailLoad', function(data));
     * document.addEventListener('onAdPresent', function(data));
     * document.addEventListener('onAdDismiss', function(data));
     * document.addEventListener('onAdLeaveApp', function(data));
     */
    private class CBAdEventDelegate extends ChartboostDelegate {
    	
    	// Interstitial Delegate Methods /////////////////////////////
    	
    	// Called after an interstitial has been displayed on the screen.
        @Override
    	public void didDisplayInterstitial(String location) {
        	fireAdEvent(EVENT_AD_PRESENT, ADTYPE_INTERSTITIAL);
        }

    	// Called after an interstitial has been loaded from the Chartboost API
    	// servers and cached locally.
    	public void didCacheInterstitial(String location) {
        	fireAdEvent(EVENT_AD_LOADED, ADTYPE_INTERSTITIAL);
        	
            if(autoShowInterstitial) {
            	showInterstitial();
            }
        }

    	// Called after an interstitial has attempted to load from the Chartboost API
    	// servers but failed.
    	public void didFailToLoadInterstitial(String location, CBImpressionError error) {
    		fireAdErrorEvent(EVENT_AD_FAILLOAD, error.ordinal(), getErrorReason(error), ADTYPE_INTERSTITIAL);
        }

    	// Called after an interstitial has been dismissed.
    	public void didDismissInterstitial(String location) {
        	fireAdEvent(EVENT_AD_DISMISS, ADTYPE_INTERSTITIAL);
        }

    	// Called after an interstitial has been closed.
    	public void didCloseInterstitial(String location){
        	
        }

    	// Called after an interstitial has been clicked.
    	public void didClickInterstitial(String location) {
    		fireAdEvent(EVENT_AD_LEAVEAPP, ADTYPE_INTERSTITIAL);
        }
    	
    	// Video Delegate Methods /////////////////////////////
    	
    	// Called after a rewarded video has been loaded from the Chartboost API
    	// servers and cached locally.
    	public void didCacheRewardedVideo(String location) {
        	fireAdEvent(EVENT_AD_LOADED, "video");
        	
            if(autoShowInterstitial) {
            	showInterstitial();
            }
        }

    	// Called after a rewarded video has attempted to load from the Chartboost API
    	// servers but failed.
    	public void didFailToLoadRewardedVideo(String location, CBImpressionError error) {
    		fireAdErrorEvent(EVENT_AD_FAILLOAD, error.ordinal(), getErrorReason(error), "video");
        }

    	// Called after a rewarded video has been dismissed.
    	public void didDismissRewardedVideo(String location) {
        	fireAdEvent(EVENT_AD_DISMISS, "video");
        }

    	// Called after a rewarded video has been closed.
    	public void didCloseRewardedVideo(String location) {
    		
    	}

    	// Called after a rewarded video has been clicked.
    	public void didClickRewardedVideo(String location) {
    		fireAdEvent(EVENT_AD_LEAVEAPP, "video");
        }

    	// Called after a rewarded video has been viewed completely and user is eligible for reward.
    	public void didCompleteRewardedVideo(String location, int reward) {
    		//fireAdEvent(EVENT_AD_LEAVEAPP, ADTYPE_INTERSTITIAL);
        }

    	// MoreApps Delegate Methods /////////////////////////////
    	
    	// Called after a MoreApps page has been loaded from the Chartboost API
		// servers and cached locally.
		public void didCacheMoreApps(String location) {
        	fireAdEvent(EVENT_AD_LOADED, "moreapps");
            if(autoShowInterstitial) {
            	showInterstitial();
            }
        }
		
		// Called after a MoreApps page has been dismissed.
		public void didDismissMoreApps(String location) {
        	fireAdEvent(EVENT_AD_DISMISS, "moreapps");
        }
		
		// Called after a MoreApps page has been closed.
		public void didCloseMoreApps(String location) {
			
		}
		
		// Called after a MoreApps page has been clicked.
		public void didClickMoreApps(String location) {
    		fireAdEvent(EVENT_AD_LEAVEAPP, "moreapps");
        }
		
		// Called after a MoreApps page attempted to load from the Chartboost API
		// servers but failed.
		public void didFailToLoadMoreApps(String location, CBImpressionError error) {
    		fireAdErrorEvent(EVENT_AD_FAILLOAD, error.ordinal(), getErrorReason(error), "moreapps");
        }
    }
    
   /** Gets a string error reason from an error code. */
   public String getErrorReason(CBImpressionError error)
   {
       switch(error) {
           case INTERNAL:
               return "Unknown internal error";
           case INTERNET_UNAVAILABLE:
               return "Network is currently unavailable";
           case TOO_MANY_CONNECTIONS:
               return "Too many requests are pending for that location";
           case WRONG_ORIENTATION:
               return "Interstitial loaded with wrong orientation";
           case FIRST_SESSION_INTERSTITIALS_DISABLED:
               return "Interstitial disabled, first session";
           case NETWORK_FAILURE:
               return "Network request failed";
           case NO_AD_FOUND:
               return "No ad received";
           case SESSION_NOT_STARTED:
               return "Session not started";
           case IMPRESSION_ALREADY_VISIBLE: 
        	   return "Impression already visible";
           case NO_HOST_ACTIVITY:
        	   return "No host activity";
           case USER_CANCELLATION:
               return "User manually cancelled the impression";
           case INVALID_LOCATION:
               return "No location detected";
           case VIDEO_UNAVAILABLE:
               return "No location detected";
           case VIDEO_ID_MISSING:
               return "No location detected";
           case ERROR_PLAYING_VIDEO:
               return "No location detected";
           case INVALID_RESPONSE:
               return "No location detected";
           case ERROR_CREATING_VIEW:
               return "No location detected";
           case ERROR_DISPLAYING_VIEW:
               return "No location detected";
           case ASSETS_DOWNLOAD_FAILURE:
               return "Prefetching is not finished";
       }
       return "Unknown error";
   }

}
