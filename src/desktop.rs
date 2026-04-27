use serde::de::DeserializeOwned;
use tauri::{AppHandle, Runtime, plugin::PluginApi, utils::acl::Number};

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
  pub fn initialize(&self) -> crate::Result<BooleanResponse> {
    Ok(BooleanResponse {
      value: Some(false),
    })
  }
  pub fn can_request_ads(&self) -> crate::Result<BooleanResponse> {
    Ok(BooleanResponse {
      value: Some(false),
    })
  }
  pub fn get_consent_status(&self) -> crate::Result<NumberResponse> {
    Ok(NumberResponse {
      value: 0,
    })
  }
  pub fn request_consent(&self) -> crate::Result<BooleanResponse> {
    Ok(BooleanResponse {
      value: Some(false),
    })
  }
  pub fn load_banner(&self, payload: LoadBannerRequest) -> crate::Result<BooleanResponse> {
    Ok(BooleanResponse {
      value: Some(false),
    })
  }
  pub fn load_interstitial(&self, payload: InterstitialRequest) -> crate::Result<BooleanResponse> {
    Ok(BooleanResponse {
      value: Some(false),
    })
  }
}
