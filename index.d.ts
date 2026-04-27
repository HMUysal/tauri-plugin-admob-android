/**
 * Ad Position on the screen.
 */
export type AdPosition = "top" | "bottom";

/**
 * Consent status as strings.
 */
export type ConsentStatus =
  | /* 0 */ "UNKNOWN"
  | /* 1 */ "NOT_REQUIRED"
  | /* 2 */ "REQUIRED"
  | /* 3 */ "OBTAINED";

/**
 * Standard response wrapper for boolean results.
 */
export interface BooleanResponse {
  value?: boolean;
}

/**
 * Standard response wrapper for string results.
 */
export interface StringResponse {
  value?: string;
}

/**
 * Consent status mapping.
 */
export const CONSENT_STATUS_MAP: [
  /* 0 */ "UNKNOWN",
  /* 1 */ "NOT_REQUIRED",
  /* 2 */ "REQUIRED",
  /* 3 */ "OBTAINED",
];
/**
 * Test command to check if the plugin is responsive.
 */
export function ping(value?: string): Promise<StringResponse>;

/**
 * Initializes the AdMob SDK and setup the consent process.
 */
export function initialize(): Promise<BooleanResponse>;

/**
 * Checks if ads can be requested based on consent.
 */
export function canRequestAds(): Promise<BooleanResponse>;

/**
 * Gets the current consent status from the SDK.
 */
export function getConsentStatus(): Promise<ConsentStatus>;

/**
 * Requests user consent for personalized ads (GDPR/UMP).
 */
export function requestConsent(): Promise<BooleanResponse>;

/**
 * Loads and displays a banner ad at the specified position.
 */
export function loadBanner(options: {
  position?: AdPosition;
  adUnitId: string;
}): Promise<BooleanResponse>;

/**
 * Loads an interstitial ad (full-screen).
 */
export function loadInterstitial(options: {
  adUnitId: string;
}): Promise<BooleanResponse>;
