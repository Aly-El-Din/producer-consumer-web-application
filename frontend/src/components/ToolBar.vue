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
          <button class="pi pi-arrow-up-right" :class="{selected: isAddingArrow}" @click="this.$emit('addArrow')"></button>
          <label>Thread</label>
        </div>
        <div class="btn">
          <button class="pi pi-play" @click="this.$emit('Play')"></button>
          <label>Play</label>
        </div>
        <div class="btn">
          <button class="pi pi-replay" @click="this.$emit('Replay')"></button>
          <label>Replay</label>
        </div>
        <div class="btn">
          <button class="pi pi-plus" @click="this.$emit('addProduct')"></button>
          <label>Product</label>
        </div>
      </div>
      <div v-if="NumberofProducts>0" class="minbtn">
        <button class="pi pi-minus" @click="this.$emit('remProduct')"></button>
      </div>
      <div class="prods">
          Number of products: {{ NumberofProducts  }}
      </div>
</template>
<script>
import 'primeicons/primeicons.css';

export default {
  name: 'ToolBar',
  props: {
    isAddingArrow: Boolean,
    NumberofProducts: Number,
  },
  data() {
    return {
      addingArrow: false,
    }
  }
}
</script>
<style scoped>
.selected{
  border: 4px solid rgb(9, 218, 9) !important;
  border-radius: 10px !important;
  background-color: aquamarine !important;
}
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
.prods{
  position: absolute;
  bottom: 2.4%;
  right: 26.8%;
  font-size: 2vh;
  background-color: rgba(46, 191, 239, 0.68);
  border-bottom-left-radius: 1vh;
  border-bottom-right-radius: 1vh;
  border-top: 0.3vh solid rgb(48, 157, 234);
  padding: 1vh;
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
.minbtn{
  position: absolute;
  bottom: 6%;
  right: 27%;
  height: 5%;
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