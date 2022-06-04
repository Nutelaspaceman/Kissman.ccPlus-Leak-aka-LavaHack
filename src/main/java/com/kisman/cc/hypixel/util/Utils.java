package com.kisman.cc.hypixel.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.kisman.cc.Kisman;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Utils {

  static JsonElement getJson(String jsonUrl) {
    try {
      // Shamelessly pulling data from Moulberry's website
      URL url = new URL(jsonUrl);
      URLConnection conn = url.openConnection();
      return new JsonParser().parse(new InputStreamReader(conn.getInputStream()));
    } catch (Exception e) {
      Kisman.LOGGER.error(e.getMessage(), e);
      return null;
    }
  }

  private static String formatValue(final long amount, final long div, final char suffix) {
    return new DecimalFormat(".##").format(amount / (double) div) + suffix;
  }

  public static String formatValue(final long amount) {
    if (amount >= 1_000_000_000_000_000L) {
      return formatValue(amount, 1_000_000_000_000_000L, 'q');
    } else if (amount >= 1_000_000_000_000L) {
      return formatValue(amount, 1_000_000_000_000L, 't');
    } else if (amount >= 1_000_000_000L) {
      return formatValue(amount, 1_000_000_000L, 'b');
    } else if (amount >= 1_000_000L) {
      return formatValue(amount, 1_000_000L, 'm');
    } else if (amount >= 100_000L) {
      return formatValue(amount, 1000L, 'k');
    }

    return NumberFormat.getInstance().format(amount);
  }
}
