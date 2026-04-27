<script>
  // 1. Senin paketin
  import * as admob from "tauri-plugin-admob-android-api";
  // 2. Ham Invoke
  import { invoke } from "@tauri-apps/api/core";

  let logs = "";
  let usePackage = true; // Test yöntemini belirleyen switch

  const TEST_ID_BANNER = "ca-app-pub-3940256099942544/6300978111";
  const TEST_ID_INTERSTITIAL = "ca-app-pub-3940256099942544/1033173712";

  const logToUI = (text) => {
    logs = `[${usePackage ? "PACKAGE" : "INVOKE"}] ${text}\n` + logs;
    console.log(text);
  };

  // Merkezi Karar Mekanizması
  async function run(label, command, payload = null) {
    try {
      logToUI(`${label} requesting...`);
      let res;

      if (usePackage) {
        // Kütüphane Fonksiyonlarını Çağır
        switch (command) {
          case "initialize": res = await admob.initialize(); break;
          case "get_consent_status": res = await admob.getConsentStatus(); break;
          case "request_consent": res = await admob.requestConsent(); break;
          case "can_request_ads": res = await admob.canRequestAds(); break;
          case "load_banner": res = await admob.loadBanner(payload); break;
          case "load_interstitial": res = await admob.loadInterstitial(payload); break;
        }
      } else {
        // Ham Invoke Çağır
        res = await invoke(`plugin:admob-android|${command}`, payload ? { payload } : {});
      }

      logToUI(`${label} Success: ${JSON.stringify(res)}`);
    } catch (e) {
      logToUI(`${label} Error: ${JSON.stringify(e)}`);
    }
  }
</script>

<main style="padding: 20px; font-family: sans-serif;">
  <div class="method-selector">
    <label>
      <input type="radio" bind:group={usePackage} value={true} />
      Test with Package (Your Library)
    </label>
    <label>
      <input type="radio" bind:group={usePackage} value={false} />
      Test with Raw Invoke
    </label>
  </div>

  <hr />

  <div class="button-group">
    <button on:click={() => run("Init", "initialize")}>Initialize</button>
    
    <button on:click={() => run("Status", "get_consent_status")}>
      Get Status
    </button>
    
    <button on:click={() => run("Consent", "request_consent")}>
      Request Consent
    </button>
    
    <button on:click={() => run("Check", "can_request_ads")}>
      Can Request Ads
    </button>

    <button on:click={() => run("Banner Top", "load_banner", { position: "top", adUnitId: TEST_ID_BANNER })}>
      Banner Top
    </button>

    <button on:click={() => run("Banner Bottom", "load_banner", { position: "bottom", adUnitId: TEST_ID_BANNER })}>
      Banner Bottom
    </button>

    <button on:click={() => run("Interstitial", "load_interstitial", { adUnitId: TEST_ID_INTERSTITIAL })}>
      Show Interstitial
    </button>
    
    <button class="clear-btn" on:click={() => logs = ""}>Clear Logs</button>
  </div>

  <div class="status-box">
    {logs}
  </div>
</main>

<style>
  .method-selector {
    margin-bottom: 15px;
    display: flex;
    gap: 20px;
    font-weight: bold;
  }

  .button-group {
    display: flex;
    gap: 10px;
    flex-wrap: wrap;
    margin-top: 15px;
  }

  button {
    padding: 10px 15px;
    cursor: pointer;
    background: #fff;
    border: 1px solid #444;
    color:black;
  }

  button:active {
    background: #ddd;
    color:black;
  }

  .clear-btn {
    background: #ffcccc;
    color:black;
  }

  .status-box {
    border: 1px solid #ccc;
    padding: 10px;
    margin-top: 20px;
    font-family: monospace;
    background: #222;
    color: #0f0;
    min-height: 250px;
    white-space: pre-wrap;
    overflow-y: auto;
  }
</style>