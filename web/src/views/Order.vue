<template>
  <div class="order-train">
    <span class="order-train-main">{{dailyTrainTicket.date}}</span>&nbsp;
    <span class="order-train-main">{{dailyTrainTicket.trainCode}}</span>次&nbsp;
    <span class="order-train-main">{{dailyTrainTicket.start}}</span>站
    <span class="order-train-main">({{dailyTrainTicket.startTime}})</span>&nbsp;
    <span class="order-train-main"> —— </span>&nbsp;
    <span class="order-train-main">{{dailyTrainTicket.end}}</span>站
    <span class="order-train-main">({{dailyTrainTicket.endTime}})</span>&nbsp;
  </div>

  <div class="order-train-ticket">
    <span v-for="item in seatTypes" :key="item.type">
      <span>{{item.desc}}</span>:
      <span class="order-train-ticket-main">{{item.price}} ¥</span>&nbsp;
      <span class="order-train-ticket-main">{{item.count}}</span>&nbsp;张票&nbsp;&nbsp;
      <br/>
    </span>
  </div>
</template>

<script setup>
const dailyTrainTicket = SessionStorage.get(SESSION_ORDER) || {};

const SEAT_TYPE = {YDZ:{code:"1", desc: "一等座", price:"0.4"}, EDZ:{code:"2", desc: "二等座", price:"0.3"}, RW:{code:"3", desc: "软卧", price:"0.6"}, YW:{code:"4", desc: "硬卧", price:"0.5"}};
const seatTypes = []
for (let KEY in SEAT_TYPE) {
  let key = KEY.toLowerCase();
  if (dailyTrainTicket[key] >= 0) {
    seatTypes.push({
      type: KEY,
      code: SEAT_TYPE[KEY]["code"],
      desc: SEAT_TYPE[KEY]["desc"],
      count: dailyTrainTicket[key],
      price: dailyTrainTicket[key + 'Price']
    })
  }
}
</script>

<style>
.order-train .order-train-main {
  font-size: 18px;
  font-weight: bold;
}
.order-train-ticket {
  margin-top: 20px;
  font-weight: bold;
}
.order-train-ticket .order-train-ticket-main {
  font-size: 18px;
  font-weight: bold;
  color: red;
  padding-left: 20px;
}
</style>

