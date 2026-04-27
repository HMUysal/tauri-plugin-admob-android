use tauri::{
  plugin::{Builder, TauriPlugin},
  Manager, Runtime,
};

pub use models::*;

#[cfg(desktop)]
mod desktop;
#[cfg(mobile)]
mod mobile;

mod commands;
mod error;
mod models;

pub use error::{Error, Result};

#[cfg(desktop)]
use desktop::AdmobAndroid;
#[cfg(mobile)]
use mobile::AdmobAndroid;

/// Extensions to [`tauri::App`], [`tauri::AppHandle`] and [`tauri::Window`] to access the admob-android APIs.
pub trait AdmobAndroidExt<R: Runtime> {
  fn admob_android(&self) -> &AdmobAndroid<R>;
}

impl<R: Runtime, T: Manager<R>> crate::AdmobAndroidExt<R> for T {
  fn admob_android(&self) -> &AdmobAndroid<R> {
    self.state::<AdmobAndroid<R>>().inner()
  }
}

/// Initializes the plugin.
pub fn init<R: Runtime>() -> TauriPlugin<R> {
  Builder::new("admob-android")
    .invoke_handler(tauri::generate_handler![
        commands::ping,
        commands::initialize,
        commands::can_request_ads,
        commands::get_consent_status,
        commands::request_consent,
        commands::load_banner,
        commands::load_interstitial,
    ])
    .setup(|app, api| {
      #[cfg(mobile)]
      let admob_android = mobile::init(app, api)?;
      #[cfg(desktop)]
      let admob_android = desktop::init(app, api)?;
      app.manage(admob_android);
      Ok(())
    })
    .build()
}
