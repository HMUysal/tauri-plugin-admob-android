use serde::de::DeserializeOwned;
use tauri::{
  plugin::{PluginApi, PluginHandle},
  AppHandle, Runtime,
};

use crate::models::*;

#[cfg(target_os = "ios")]
tauri::ios_plugin_binding!(init_plugin_admob_android);

// initializes the Kotlin or Swift plugin classes
pub fn init<R: Runtime, C: DeserializeOwned>(
  _app: &AppHandle<R>,
  api: PluginApi<R, C>,
) -> crate::Result<AdmobAndroid<R>> {
  #[cfg(target_os = "android")]
  let handle = api.register_android_plugin("com.plugin.admob_android", "AdmobAndroidPlugin")?;
  #[cfg(target_os = "ios")]
  let handle = api.register_ios_plugin(init_plugin_admob_android)?;
  Ok(AdmobAndroid(handle))
}

/// Access to the admob-android APIs.
pub struct AdmobAndroid<R: Runtime>(PluginHandle<R>);

impl<R: Runtime> AdmobAndroid<R> {
  pub fn ping(&self, payload: PingRequest) -> crate::Result<PingResponse> {
    self
      .0
      .run_mobile_plugin("ping", payload)
      .map_err(Into::into)
  }
}

impl<R: Runtime> AdmobAndroid<R> {
  pub fn initialize(&self) -> crate::Result<InitializeResponse> {
    self
      .0
      .run_mobile_plugin("initialize", ())
      .map_err(Into::into)
  }
}

impl<R: Runtime> AdmobAndroid<R> {
  pub fn request_consent(&self) -> crate::Result<RequestConsentResponse> {
    self
      .0
      .run_mobile_plugin("request_consent", ())
      .map_err(Into::into)
  }
}

impl<R: Runtime> AdmobAndroid<R> {
  pub fn load_banner(&self, payload: LoadBannerRequest) -> crate::Result<LoadBannerResponse> {
    self
      .0
      .run_mobile_plugin("load_banner", payload)
      .map_err(Into::into)
  }
}

impl<R: Runtime> AdmobAndroid<R> {
  pub fn load_interstitial(&self, payload: InterstitialRequest) -> crate::Result<InterstitialResponse> {
    self
      .0
      .run_mobile_plugin("load_interstitial", payload)
      .map_err(Into::into)
  }
}
