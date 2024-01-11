<template>
    <div class="ToolBar">
        <div class="btn">
          <button class="pi pi-cog" @click="this.$emit('addMachine')"></button>
          <label>Machine</label>
        </div>
        <div class="btn">
          <button class="pi pi-server" @click="this.$emit('addQueue')"></button>
          <label>Queue</label>
        </div>
        <div class="btn">
          <button class="pi pi-arrow-up-right" @click="this.$emit('addArrow')"></button>
          <label>Thread</label>
        </div>
        <div class="btn">
          <button class="pi pi-play"></button>
          <label>Play</label>
        </div>
        <div class="btn">
          <button class="pi pi-replay"></button>
          <label>Replay</label>
        </div>
        <div class="btn">
          <button class="pi pi-plus"></button>
          <label>Product</label>
        </div>
    </div>
</template>
<script>
import 'primeicons/primeicons.css';

export default {
  name: 'ToolBar',
}
</script>
<style scoped>
.ToolBar {
  display: flex;
  flex-direction: row;
  justify-content: space-evenly;
  align-items: center;
  position: absolute;
  bottom: 7%;
  left: 25%;
  background-color: rgba(46, 191, 239, 0.68);
  border-radius: 15vh;
  width: 50%;
  height: 8%;
}
.btn{
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 7%;
  height: 100%;
  padding-top: 0.5%;
}
label{
  font-size: 1.5vh;
  font-weight: bold;
  color: rgb(0, 0, 0);
  padding-top: 1;
}
button{
  background-color: rgb(255, 255, 255);
  font-size: 2.5vh;
  border-radius: 1.5vh;
  width: 100%;
  height: 70%;
  font-weight: bold;
  cursor: pointer;
}
button:hover{
  background-color: rgb(202, 192, 192);
  border-radius: 1.5vh;
  width: 100%;
  height: 70%;
  font-weight: bold;
  cursor: pointer;
  box-shadow: 0 0 0 0.2rem rgba(11, 77, 148, 0.734);
}
</style>