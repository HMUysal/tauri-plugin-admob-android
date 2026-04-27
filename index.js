import { invoke } from "@tauri-apps/api/core";

/**
 * Test command to check if the plugin is responsive.
 * @param {string} [value] - An optional string value to be echoed back.
 * @returns {Promise<{value?: string}>}
 */
export async function ping(value) {
  return await invoke("plugin:admob-android|ping", {
    value,
  });
}

/**
 * Initializes the AdMob SDK.
 * @returns {Promise<{value?: boolean}>}
 */
export async function initialize() {
  return await invoke("plugin:admob-android|initialize");
}

/**
 * Requests user consent for personalized ads (GDPR/UMP).
 * @returns {Promise<{value?: boolean}>}
 */
export async function requestConsent() {
  return await invoke("plugin:admob-android|request_consent");
}

/**
 * Loads and displays a banner ad at the specified position.
 * @param {Object} options
 * @param {'top' | 'bottom'} options.position - The screen position of the banner.
 * @param {string} options.adUnitId - The AdMob unit ID for the banner.
 * @returns {Promise<{value?: boolean}>}
 */
export async function loadBanner({ position = "bottom", adUnitId }) {
  return await invoke("plugin:admob-android|load_banner", {
    payload: {
      position,
      adUnitId,
    },
  });
}

/**
 * Loads an interstitial ad (full-screen).
 * @param {Object} options
 * @param {string} options.adUnitId - The AdMob unit ID for the interstitial.
 * @returns {Promise<{value?: boolean}>}
 */
export async function loadInterstitial({ adUnitId }) {
  return await invoke("plugin:admob-android|load_interstitial", {
    payload: {
      adUnitId,
    },
  });
}
