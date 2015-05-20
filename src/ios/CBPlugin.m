//
//  CDVAdMobPlugin.m
//  TestAdMobCombo
//
//  Created by Xie Liming on 14-10-20.
//
//

#import <CommonCrypto/CommonDigest.h>
#import <AdSupport/AdSupport.h>

#import <Chartboost/Chartboost.h>
#import <Chartboost/CBNewsfeed.h>

#import "CBPlugin.h"

#define TEST_BANNER_ID           @"banner/not_supported"
#define TEST_INTERSTITIALID      @"interstitial/Home Screen"

#define TEST_APPID              @"543f9ac9c26ee436e7133794"
#define TEST_APPKEY             @"6d1c2e4a5d1225825a7472aa52d0f45e4757de39"

#define BANNER_AD_WIDTH         320
#define BANNER_AD_HEIGHT        50

@interface CBPlugin()<ChartboostDelegate>

@property (nonatomic, retain) NSString* mAppId;
@property (nonatomic, retain) NSString* mAppKey;

@property (assign) bool mAppIdSet;

- (void) validateChartboost;

@end

@implementation CBPlugin

- (void)pluginInitialize
{
    [super pluginInitialize];
    
    self.adWidth = BANNER_AD_WIDTH;
    self.adHeight = BANNER_AD_HEIGHT;
    
    self.mAppId = TEST_APPID;
    self.mAppKey = TEST_APPKEY;
    
    self.testTraffic = FALSE;
    
    self.mAppIdSet = FALSE;
}

- (NSString*) __getProductShortName { return @"Chartboost"; }

- (NSString*) __getTestBannerId {
    return TEST_BANNER_ID;
}
- (NSString*) __getTestInterstitialId {
    return TEST_INTERSTITIALID;
}

- (void) validateChartboost
{
    if(! self.mAppIdSet) {
        [Chartboost startWithAppId:self.mAppId
                      appSignature:self.mAppKey
                          delegate:self];
        
        self.mAppIdSet = TRUE;
    }
}

- (void) parseOptions:(NSDictionary *)options
{
    NSLog(@"parseOptions");
    
    [super parseOptions:options];
    
    if(self.isTesting) {
    }
    
    NSString* str = [options objectForKey:OPT_APP_ID];
    if(str) self.mAppId = str;
    
    str = [options objectForKey:OPT_APP_KEY];
    if(str) self.mAppKey = str;
}

- (UIView*) __createAdView:(NSString*)adId
{
    return [self getView];
}

- (void) __showBanner:(int) position atX:(int)x atY:(int)y
{
}

- (int) __getAdViewWidth:(UIView*)view {
    return view.frame.size.width;
}

- (int) __getAdViewHeight:(UIView*)view {
    return view.frame.size.height;
}

- (void) __loadAdView:(UIView*)view
{
}

- (void) __pauseAdView:(UIView*)view {
}

- (void) __resumeAdView:(UIView*)view {
}

- (void) __destroyAdView:(UIView*)view {
    if(! view) return;
    
}

- (NSObject*) __createInterstitial:(NSString*)adId {
    NSLog(@"__createInterstitial");
    
    [self validateChartboost];
    
    NSArray* fields = [adId componentsSeparatedByString:@"/"];
    if([fields count] >= 2) {
        //NSString* adType = [fields objectAtIndex:0];
        //NSString* adLocation = [fields objectAtIndex:1];
        // we use the ad type and ad location as interstitial id
        return adId;
    }
    
    return nil;
}

- (void) __loadInterstitial:(NSObject*)interstitial {
    NSLog(@"__loadInterstitial");
    
    if(! interstitial) return;
    NSString* adId = (NSString*) interstitial;
    
    NSArray* fields = [adId componentsSeparatedByString:@"/"];
    if([fields count] >= 2) {
        NSString* adType = [fields objectAtIndex:0];
        NSString* adLocation = [fields objectAtIndex:1];
        
        if (adType!=nil && [adType compare:@"video"] == NSOrderedSame) {
            [Chartboost cacheRewardedVideo:adLocation];
            
        } else if (adType!=nil && [adType compare:@"moreapps"] == NSOrderedSame) {
            [Chartboost cacheMoreApps:adLocation];
            
        } else {
            [Chartboost cacheInterstitial:adLocation];
            
        }
    }
}

- (void) __showInterstitial:(NSObject*)interstitial {
    NSLog(@"__showInterstitial");
    
    if(! interstitial) return;
    NSString* adId = (NSString*) interstitial;
    
    NSArray* fields = [adId componentsSeparatedByString:@"/"];
    if([fields count] >= 2) {
        NSString* adType = [fields objectAtIndex:0];
        NSString* adLocation = [fields objectAtIndex:1];
        
        if (adType!=nil && [adType compare:@"video"] == NSOrderedSame) {
            if([Chartboost hasRewardedVideo:adLocation]) {
                [Chartboost showRewardedVideo:adLocation];
            }
            
        } else if (adType!=nil && [adType compare:@"moreapps"] == NSOrderedSame) {
            if([Chartboost hasMoreApps:adLocation]) {
                [Chartboost showMoreApps:adLocation];
            }
            
        } else {
            if([Chartboost hasInterstitial:adLocation]) {
                [Chartboost showInterstitial:adLocation];
            }
        }
    }
}

- (void) __destroyInterstitial:(NSObject*)interstitial {
    if(! interstitial) return;
    // NSString* adId = (NSString*) interstitial;
    // do nothing
}

#pragma mark GADBannerViewDelegate implementation

/**
 * document.addEventListener('onAdLoaded', function(data));
 * document.addEventListener('onAdFailLoad', function(data));
 * document.addEventListener('onAdPresent', function(data));
 * document.addEventListener('onAdDismiss', function(data));
 * document.addEventListener('onAdLeaveApp', function(data));
 */

- (NSString*) getErrorReason:(CBLoadError)error
{
    switch(error) {
        case CBLoadErrorInternal:
            return @"Unknown internal error";
        case CBLoadErrorInternetUnavailable:
            return @"Network is currently unavailable";
        case CBLoadErrorTooManyConnections:
            return @"Too many requests are pending for that location";
        case CBLoadErrorWrongOrientation:
            return @"Interstitial loaded with wrong orientation";
        case CBLoadErrorFirstSessionInterstitialsDisabled:
            return @"Interstitial disabled, first session";
        case CBLoadErrorNetworkFailure:
            return @"Network request failed";
        case CBLoadErrorNoAdFound:
            return @"No ad received";
        case CBLoadErrorSessionNotStarted:
            return @"Session not started";
        case CBLoadErrorUserCancellation:
            return @"User manually cancelled the impression";
        case CBLoadErrorNoLocationFound:
            return @"No location detected";
        case CBLoadErrorPrefetchingIncomplete:
            return @"Video Prefetching is not finished";
    }
    return @"Unknown error";
}

- (void)didFailToLoadInterstitial:(CBLocation)location withError:(CBLoadError)error
{
    NSLog(@"%d - %@", (int)error, [self getErrorReason:error]);

    [self fireAdErrorEvent:EVENT_AD_FAILLOAD withCode:(int)error
                   withMsg:[self getErrorReason:error]
                  withType:ADTYPE_INTERSTITIAL];
}

- (void)didCacheInterstitial:(CBLocation)location
{
    if (self.interstitial && self.autoShowInterstitial) {
        dispatch_async(dispatch_get_main_queue(), ^{
            [self __showInterstitial:[NSString stringWithFormat:@"interstitial/%@", location]];
        });
    }
    [self fireAdEvent:EVENT_AD_LOADED withType:ADTYPE_INTERSTITIAL];
}

- (void)didDisplayInterstitial:(CBLocation)location
{
    [self fireAdEvent:EVENT_AD_PRESENT withType:ADTYPE_INTERSTITIAL];
}

- (void)didDismissInterstitial:(CBLocation)location
{
    [self fireAdEvent:EVENT_AD_DISMISS withType:ADTYPE_INTERSTITIAL];
}

- (void)didCloseInterstitial:(CBLocation)location
{
    //[self fireAdEvent:EVENT_AD_DISMISS withType:ADTYPE_INTERSTITIAL];
}

- (void)didClickInterstitial:(CBLocation)location
{
    [self fireAdEvent:EVENT_AD_LEAVEAPP withType:ADTYPE_INTERSTITIAL];
}

- (void)didFailToLoadRewardedVideo:(CBLocation)location withError:(CBLoadError)error
{
    NSLog(@"%d - %@", (int)error, [self getErrorReason:error]);
    
    [self fireAdErrorEvent:EVENT_AD_FAILLOAD withCode:(int)error
                   withMsg:[self getErrorReason:error]
                  withType:@"video"];
}

- (void)didCacheRewardedVideo:(CBLocation)location
{
    if (self.interstitial && self.autoShowInterstitial) {
        dispatch_async(dispatch_get_main_queue(), ^{
            [self __showInterstitial:[NSString stringWithFormat:@"video/%@", location]];
        });
    }
    [self fireAdEvent:EVENT_AD_LOADED withType:@"video"];
}

- (void)didDisplayRewardedVideo:(CBLocation)location
{
    [self fireAdEvent:EVENT_AD_PRESENT withType:@"video"];
}

- (void)didDismissRewardedVideo:(CBLocation)location
{
    [self fireAdEvent:EVENT_AD_DISMISS withType:@"video"];
}

- (void)didCloseRewardedVideo:(CBLocation)location
{
    //[self fireAdEvent:EVENT_AD_DISMISS withType:@"video"];
}

- (void)didClickRewardedVideo:(CBLocation)location
{
    [self fireAdEvent:EVENT_AD_LEAVEAPP withType:@"video"];
}

// ---------------------------------------------------------------

- (void)didFailToLoadMoreApps:(CBLocation)location withError:(CBLoadError)error
{
    NSLog(@"%d - %@", (int)error, [self getErrorReason:error]);
    
    [self fireAdErrorEvent:EVENT_AD_FAILLOAD withCode:(int)error
                   withMsg:[self getErrorReason:error]
                  withType:@"moreapps"];
}

- (void)didCacheMoreApps:(CBLocation)location
{
    if (self.interstitial && self.autoShowInterstitial) {
        dispatch_async(dispatch_get_main_queue(), ^{
            [self __showInterstitial:[NSString stringWithFormat:@"moreapps/%@", location]];
        });
    }
    [self fireAdEvent:EVENT_AD_LOADED withType:@"moreapps"];
}

- (void)didDisplayMoreApps:(CBLocation)location
{
    [self fireAdEvent:EVENT_AD_PRESENT withType:@"moreapps"];
}

- (void)didDismissMoreApps:(CBLocation)location
{
    [self fireAdEvent:EVENT_AD_DISMISS withType:@"moreapps"];
}

- (void)didCloseMoreApps:(CBLocation)location
{
    //[self fireAdEvent:EVENT_AD_DISMISS withType:@"moreapps"];
}

- (void)didClickMoreApps:(CBLocation)location
{
    [self fireAdEvent:EVENT_AD_LEAVEAPP withType:@"moreapps"];
}

@end
