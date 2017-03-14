/*
 * Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 *
 */

package com.google.android.apps.remixer;

import android.support.annotation.DrawableRes;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;


public class TransactionItem {

  private final Business business;
  private final Date date;
  // I know floats are terrible for currency, but this is a simple demo app.
  private final float amount;

  public TransactionItem(Business business, Date date, float amount) {
    this.business = business;
    this.date = date;
    this.amount = amount;
  }

  public enum Business {
    BARCADE(R.drawable.ic_bar, "Barcade"),
    BATHTUB_GIN(R.drawable.ic_bar, "Bathtub Gin"),
    PDT(R.drawable.ic_bar, "Please don't tell"),
    LYFT(R.drawable.ic_taxi, "Lyft"),
    NYC_TAXI(R.drawable.ic_taxi, "NYC Taxi"),
    APPLE_STORE(R.drawable.ic_shopping, "Apple Store"),
    NINTENDO_STORE(R.drawable.ic_shopping, "Nintendo Store"),
    AMAZON(R.drawable.ic_shopping, "Amazon"),
    IN_N_OUT(R.drawable.ic_fastfood, "In-N-Out"),
    FIVE_GUYS(R.drawable.ic_fastfood, "Five Guys"),
    STUMPTOWN(R.drawable.ic_cafe, "Stumptown Coffee Roasters"),
    BLUE_BOTTLE(R.drawable.ic_fastfood, "Blue Bottle Coffee");

    @DrawableRes final int iconResource;
    final String name;

    Business(@DrawableRes int iconResource, String name) {
      this.iconResource = iconResource;
      this.name = name;
    }
  }

  public String getBusinessName() {
    return business.name;
  }

  public Date getDate() {
    return date;
  }

  public float getAmount() {
    return amount;
  }

  @DrawableRes
  public int getBusinessTypeIconResource(){
    return this.business.iconResource;
  }

  // These members below aid in generating a random list of transactions.

  private static final Random RANDOM = new Random();
  private static final Business[] BUSINESSES = Business.values();

  private static Business getRandomBusiness() {
    return BUSINESSES[RANDOM.nextInt(BUSINESSES.length)];
  }

  private static float getRandomAmount() {
    // Only return values up to 99.99f
    return RANDOM.nextInt(9999) / 100f;
  }

  private static Date getDateInLast3Days(Date today) {
    int daysBefore = RANDOM.nextInt(4);
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(today);
    calendar.add(Calendar.DATE, -daysBefore);
    return calendar.getTime();
  }

  public static TransactionItem[] generateTransactions(int amount) {
    Date today = new Date();
    TransactionItem[] transactions = new TransactionItem[amount];
    for (int i = 0; i < amount; i++) {
      today = getDateInLast3Days(today);
      transactions[i] = new TransactionItem(getRandomBusiness(), today, getRandomAmount());
    }
    return transactions;
  }
}
