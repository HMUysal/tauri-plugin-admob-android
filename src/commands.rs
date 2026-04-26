use tauri::{AppHandle, command, Runtime};

use crate::models::*;
use crate::Result;
use crate::AdmobAndroidExt;

#[command]
pub(crate) async fn ping<R: Runtime>(
    app: AppHandle<R>,
    payload: PingRequest,
) -> Result<PingResponse> {
    app.admob_android().ping(payload)
}

#[command]
pub(crate) async fn initialize<R: Runtime>(
    app: AppHandle<R>,
) -> Result<InitializeResponse> {
    app.admob_android().initialize()
}

#[command]
pub(crate) async fn request_consent<R: Runtime>(
    app: AppHandle<R>,
) -> Result<RequestConsentResponse> {
    app.admob_android().request_consent()
}

#[command]
pub(crate) async fn load_banner<R: Runtime>(
    app: AppHandle<R>,
    payload: LoadBannerRequest,
) -> Result<LoadBannerResponse> {
    app.admob_android().load_banner(payload)
}


#[command]
pub(crate) async fn load_interstitial<R: Runtime>(
    app: AppHandle<R>,
    payload: InterstitialRequest,
) -> Result<InterstitialResponse> {
    app.admob_android().load_interstitial(payload)
}
