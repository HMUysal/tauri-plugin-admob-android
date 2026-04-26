use serde::de::DeserializeOwned;
use tauri::{plugin::PluginApi, AppHandle, Runtime};

use crate::models::*;

pub fn init<R: Runtime, C: DeserializeOwned>(
  app: &AppHandle<R>,
  _api: PluginApi<R, C>,
) -> crate::Result<AdmobAndroid<R>> {
  Ok(AdmobAndroid(app.clone()))
}

/// Access to the admob-android APIs.
pub struct AdmobAndroid<R: Runtime>(AppHandle<R>);

impl<R: Runtime> AdmobAndroid<R> {
  pub fn ping(&self, payload: PingRequest) -> crate::Result<PingResponse> {
    Ok(PingResponse {
      value: payload.value,
    })
  }
  pub fn initialize(&self) -> crate::Result<InitializeResponse> {
    Ok(InitializeResponse {
      value: Some(false),
    })
  }
  pub fn request_consent(&self) -> crate::Result<RequestConsentResponse> {
    Ok(RequestConsentResponse {
      value: Some(false),
    })
  }
  pub fn load_banner(&self, payload: LoadBannerRequest) -> crate::Result<LoadBannerResponse> {
    Ok(LoadBannerResponse {
      value: Some(false),
    })
  }
  pub fn load_interstitial(&self, payload: InterstitialRequest) -> crate::Result<InterstitialResponse> {
    Ok(InterstitialResponse {
      value: Some(false),
    })
  }
}
