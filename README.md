# Cordova plugin for Chartboost #

### Show Mobile Ad with single line of javascript code ###

Step 1: Create Ad Unit Id for your game, then write it in your javascript code.

```javascript
// select the right Ad Id according to platform
    var adunits = {};
    if( /(android)/i.test(navigator.userAgent) ) { // for android
		adunits = {
			banner: 'appid/appkey', 
			interstitial: 'appid/appkey', 
        };
    } else if(/(ipod|iphone|ipad)/i.test(navigator.userAgent)) { // for ios
		adunits = {
			banner: 'appid/appkey', 
			interstitial: 'appid/appkey',
		};
    } else { // for windows phone
		console.log('windows phone not supported');
    }
```

Step 2: Want a banner? single line of javascript code.

```javascript
// it will display smart banner at top center, using the default options
if(Chartboost) Chartboost.createBanner( {
	adId: adunits.banner, 
	position: Chartboost.AD_POSITION.TOP_CENTER, 
	autoShow: true } );
```

Step 3: Want full screen Ad? Easy, 2 lines of code. 

```javascript
// preppare and load ad resource in background, e.g. at begining of game level
if(Chartboost) Chartboost.prepareInterstitial( {adId:adunits.interstitial, autoShow:false} );

// show the interstitial later, e.g. at end of game level
if(Chartboost) Chartboost.showInterstitial();
```

### Features ###

Platforms supported:
- [x] Android
- [x] iOS

Highlights:
- [x] Easy-to-use: Display Ad with single line of javascript code.
- [x] Powerful: Support interstitial, and video Ad.
- [x] Flexible: Fixed and overlapped mode, put banner at any position with overlap mode.
- [x] Smart: Auto fit on orientation change.
- [x] Same API: Exactly same API with other Ad plugins, easy to switch from one Ad service to another.
- [x] Up to date: Latest SDK and Android Google play services.
- [x] Good support: Actively maintained, prompt response.

## How to use? ##

* If use with Cordova CLI:
```bash
cordova plugin add cordova-plugin-chartboost
```

* If use with PhoneGap Buid, just configure in config.xml:
```javascript
<gap:plugin name="com.rjfun.cordova.chartboost" source="plugins.cordova.io"/>
```

* If use with Intel XDK:
Project -> CORDOVA 3.X HYBRID MOBILE APP SETTINGS -> PLUGINS AND PERMISSIONS -> Third-Party Plugins ->
Add a Third-Party Plugin -> Get Plugin from the Web, input:
```
Name: ChartboostPlugin
Plugin ID: com.rjfun.cordova.chartboost
[x] Plugin is located in the Apache Cordova Plugins Registry
```

## Quick start with cordova CLI ##
```bash
	# create a demo project
    cordova create test1 com.rjfun.test1 Test1
    cd test1
    cordova platform add android
    cordova platform add ios

    # now add the plugin, cordova CLI will handle dependency automatically
    cordova plugin add cordova-plugin-chartboost

    # now remove the default www content, copy the demo html file to www
    rm -r www/*;
    cp plugins/cordova-plugin-chartboost/test/* www/;

	# now build and run the demo in your device or emulator
    cordova prepare; 
    cordova run android; 
    cordova run ios;
    # or import into Xcode / eclipse
```

## Javascript API Overview ##

Methods:
```javascript
// use banner
createBanner(adId/options, success, fail);
removeBanner();
showBanner(position);
showBannerAtXY(x, y);
hideBanner();

// use interstitial
prepareInterstitial(adId/options, success, fail);
showInterstitial();

// set default value for other methods
setOptions(options, success, fail);
```

## Screenshots ##


## See Also ##

Ad PluginPro series for the world leading Mobile Ad services:

* [GoogleAds PluginPro](https://github.com/floatinghotpot/cordova-admob-pro), for Google AdMob/DoubleClick.
* [iAd PluginPro](https://github.com/floatinghotpot/cordova-iad-pro), for Apple iAd. 
* [FacebookAds PluginPro](https://github.com/floatinghotpot/cordova-plugin-facebookads), for Facebook Audience Network.
* [FlurryAds PluginPro](https://github.com/floatinghotpot/cordova-plugin-flurry), for Flurry Ads.
* [mMedia PluginPro](https://github.com/floatinghotpot/cordova-plugin-mmedia), for Millennial Meida.
* [MobFox PluginPro](https://github.com/floatinghotpot/cordova-mobfox-pro), for MobFox.
* [MoPub PluginPro](https://github.com/floatinghotpot/cordova-plugin-mopub), for MoPub.
* [Chartboost PluginPro](https://github.com/floatinghotpot/cordova-plugin-chartboost), for Chartboost.

More Cordova/PhoneGap plugins by Raymond Xie, [find them in plugin registry](http://plugins.cordova.io/#/search?search=rjfun).

If use in commercial project and need email/skype support, please [buy a license](http://rjfun.github.io/), you will be supported with high priority.

Project outsourcing and consulting service is also available. Please [contact us](mailto:rjfun.mobile@gmail.com) if you have the business needs.

