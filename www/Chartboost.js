
var argscheck = require('cordova/argscheck'),
    exec = require('cordova/exec');

var cbExport = {};

cbExport.AD_LOCATION = {
  CBLocationStartup: 'Startup',
  CBLocationHomeScreen: 'Home Screen',
  CBLocationMainMenu: 'Main Menu',
  CBLocationGameScreen: 'Game Screen',
  CBLocationAchievements: 'Achievements',
  CBLocationQuests: 'Quests',
  CBLocationPause: 'Pause',
  CBLocationLevelStart: 'Level Start',
  CBLocationLevelComplete: 'Level Complete',
  CBLocationTurnComplete: 'Turn Complete',
  CBLocationIAPStore: 'IAP Store',
  CBLocationItemStore: 'Item Store',
  CBLocationGameOver: 'Game Over',
  CBLocationLeaderBoard: 'Leaderboard',
  CBLocationSettings: 'Settings',
  CBLocationQuit: 'Quit',
  CBLocationDefault: 'Default',
};

/*
 * set options:
 *  {
 *    appId: string,	// app id
 *    appKey: string,	// app key / signature
 *    isTesting: boolean,	// if set to true, to receive test ads
 *    autoShow: boolean,	// if set to true, no need call showBanner or showInterstitial
 *   }
 */
cbExport.setOptions = function(options, successCallback, failureCallback) {
	  if(typeof options === 'object') {
		  cordova.exec( successCallback, failureCallback, 'Chartboost', 'setOptions', [options] );
	  } else {
		  if(typeof failureCallback === 'function') {
			  failureCallback('options should be specified.');
		  }
	  }
	};

cbExport.createBanner = function(args, successCallback, failureCallback) {
	var options = {};
	if(typeof args === 'object') {
		for(var k in args) {
			if(k === 'success') { if(args[k] === 'function') successCallback = args[k]; }
			else if(k === 'error') { if(args[k] === 'function') failureCallback = args[k]; }
			else {
				options[k] = args[k];
			}
		}
	} else if(typeof args === 'string') {
		options = { adId: args };
	}
	cordova.exec( successCallback, failureCallback, 'Chartboost', 'createBanner', [ options ] );
};

cbExport.removeBanner = function(successCallback, failureCallback) {
	cordova.exec( successCallback, failureCallback, 'Chartboost', 'removeBanner', [] );
};

cbExport.hideBanner = function(successCallback, failureCallback) {
	cordova.exec( successCallback, failureCallback, 'Chartboost', 'hideBanner', [] );
};

cbExport.showBanner = function(position, successCallback, failureCallback) {
	if(typeof position === 'undefined') position = 0;
	cordova.exec( successCallback, failureCallback, 'Chartboost', 'showBanner', [ position ] );
};

cbExport.showBannerAtXY = function(x, y, successCallback, failureCallback) {
	if(typeof x === 'undefined') x = 0;
	if(typeof y === 'undefined') y = 0;
	cordova.exec( successCallback, failureCallback, 'Chartboost', 'showBannerAtXY', [{x:x, y:y}] );
};

cbExport.prepareInterstitial = function(args, successCallback, failureCallback) {
	var options = {};
	if(typeof args === 'object') {
		for(var k in args) {
			if(k === 'success') { if(args[k] === 'function') successCallback = args[k]; }
			else if(k === 'error') { if(args[k] === 'function') failureCallback = args[k]; }
			else {
				options[k] = args[k];
			}
		}
	} else if(typeof args === 'string') {
		options = { adId: args };
	}
	cordova.exec( successCallback, failureCallback, 'Chartboost', 'prepareInterstitial', [ args ] );
};

cbExport.showInterstitial = function(successCallback, failureCallback) {
	cordova.exec( successCallback, failureCallback, 'Chartboost', 'showInterstitial', [] );
};

cbExport.cbInterstitial = function(adlocation, successCallback, failureCallback) {
    var args = {
        adId: 'interstitial/' + adlocation,
        autoShow: true
    };
    cordova.exec( successCallback, failureCallback, 'Chartboost', 'prepareInterstitial', [ args ] );
};

cbExport.cbRewardedVideo = function(adlocation, successCallback, failureCallback) {
    var args = {
        adId: 'video/' + adlocation,
        autoShow: true
    };
    cordova.exec( successCallback, failureCallback, 'Chartboost', 'prepareInterstitial', [ args ] );
};

cbExport.cbMoreApps = function(adlocation, successCallback, failureCallback) {
    var args = {
        adId: 'moreapps/' + adlocation,
        autoShow: true
    };
    cordova.exec( successCallback, failureCallback, 'Chartboost', 'prepareInterstitial', [ args ] );
};

module.exports = cbExport;

