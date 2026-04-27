/**
 * Ad Position on the screen.
 */
export type AdPosition = "top" | "bottom";

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
 * Test command to check if the plugin is responsive.
 */
export function ping(value?: string): Promise<StringResponse>;

/**
 * Initializes the AdMob SDK and setup the consent process.
 */
export function initialize(): Promise<BooleanResponse>;

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
