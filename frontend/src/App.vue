<template>
  <div id="app">
    <div ref="stage" id="container"></div>
    <ToolBar @addMachine="addMachine" @addQueue="addQueue" @addArrow="isAddingArrow = !isAddingArrow"
      @addProduct="NumberofProducts = NumberofProducts + 1" @remProduct="NumberofProducts = NumberofProducts - 1"
      @play="play" @replay="replay" :isAddingArrow="isAddingArrow" :NumberofProducts="NumberofProducts" />
  </div>
</template>

<script>
import ToolBar from './components/ToolBar.vue'
import Konva from 'konva'
import { Stomp } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
export default {
  name: 'App',
  components: {
    ToolBar
  },
  data() {
    return {
      stompClient: null,
      stage: null,
      layer: null,
      machines: [],
      queues: [],
      arrows: [],
      arrowsGroup: [],
      nextMachineId: 1,
      nextQueueId: 0,
      isAddingArrow: false,
      selectedSource: null,
      selectedDestination: null,
      NumberofProducts: 0,
    }
  },
  created() {
    const socket = new SockJS('http://localhost:8080/websocket-endpoint');
    this.stompClient = Stomp.over(socket);
    this.stompClient.connect({}, () => {
      this.stompClient.subscribe('/topic/updates', (update) => {
        console.log("here in update ")
        console.log(update)
        console.log(update.body)
        if (update.body.split(',')[0][0] === 'Q')
          this.queues[parseInt(update.body.split(',')[0][1])].children[2].text(update.body.split(',')[1]);
        else {
          this.machines[parseInt(update.body.split(',')[0][1]) - 1].children[0].fill(update.body.split(',')[1]);
        }
      });
    });
  },
  mounted() {
    this.stage = new Konva.Stage({
      container: 'container',
      width: 1520,
      height: 695
    });
    this.layer = new Konva.Layer();
    this.stage.add(this.layer);
  },
  methods: {
    createShapeWithText(xc, yc, shape, textString) {
      const text = new Konva.Text({
        x: xc,
        y: yc,
        text: textString,
        fontSize: 20,
        fontFamily: 'serif',
        fontweight: 'bold',
        fill: 'black',
      });

      const group = new Konva.Group({
        draggable: true,
      });

      group.add(shape);
      group.add(text);

      if (textString[0] === 'Q') {
        const text2 = new Konva.Text({
          x: xc + 9,
          y: yc + 24,
          text: '0',
          fontSize: 15,
          fill: 'black',
          fontFamily: 'serif',
        });
        group.add(text2);
      }

      return group;
    },
    addMachine() {
      const machine = new Konva.Circle({
        x: 42,
        y: 42,
        radius: 40,
        fill: '#22d431',
        stroke: 'black',
      });

      const machineGroup = this.createShapeWithText(30, 34, machine, 'M' + this.nextMachineId);

      machineGroup.on('click', () => {
        if (this.isAddingArrow) {
          if (!this.selectedSource) {
            this.selectedSource = machineGroup;
          } else if (!this.selectedDestination) {
            this.selectedDestination = machineGroup;
            this.addArrow();
          }
        }
      });

      this.layer.add(machineGroup);
      this.machines.push(machineGroup);
      this.layer.draw();
      this.nextMachineId++;
    },
    addQueue() {
      const queue = new Konva.Rect({
        x: 5,
        y: 5,
        width: 100,
        height: 50,
        fill: 'yellow',
        stroke: 'black',
        type: 'queue'
      });

      const queueGroup = this.createShapeWithText(40, 10, queue, 'Q' + this.nextQueueId);

      queueGroup.on('click', () => {
        if (this.isAddingArrow) {
          if (!this.selectedSource) {
            this.selectedSource = queueGroup;
          } else if (!this.selectedDestination) {
            this.selectedDestination = queueGroup;
            this.addArrow();
          }
        }
      });
      this.layer.add(queueGroup);
      this.queues.push(queueGroup);
      this.layer.draw();
      this.nextQueueId++;
    },
    addArrow() {
      console.log(this.arrows);
      console.log([this.selectedSource.children[1].text(), this.selectedDestination.children[1].text()]);
      if (this.selectedDestination.children[1].text()[0] === this.selectedSource.children[1].text()[0]) {
        alert("You can't connect two machines or two queues!");
        this.selectedSource = null;
        this.selectedDestination = null;
        return;
      } else if (this.arrows.some(arrow => arrow[0] === this.selectedSource.children[1].text() && arrow[1] === this.selectedDestination.children[1].text())
        || this.arrows.some(arrow => arrow[0] === this.selectedDestination.children[1].text() && arrow[1] === this.selectedSource.children[1].text())) {
        alert("Queue and Machine Already Connected!");
        this.selectedSource = null;
        this.selectedDestination = null;
        return;
      } else if (this.selectedDestination.children[1].text() === 'Q0') {
        alert("Q0 can't be a destination!");
        this.selectedSource = null;
        this.selectedDestination = null;
        return;
      }
      if (this.selectedSource && this.selectedDestination) {
        let x1, y1, x2, y2;
        if (this.selectedSource.children[1].text()[0] === 'M') {
          x1 = this.selectedSource.x() + 40;
          y1 = this.selectedSource.y() + 40;
          x2 = this.selectedDestination.x() + 50;
          y2 = this.selectedDestination.y() + 25;
        } else {
          x1 = this.selectedSource.x() + 50;
          y1 = this.selectedSource.y() + 25;
          x2 = this.selectedDestination.x() + 40;
          y2 = this.selectedDestination.y() + 40;
        }

        var angle = Math.atan2(y2 - y1, x2 - x1);
        x1 = x1 + 50 * Math.cos(angle);
        y1 = y1 + 50 * Math.sin(angle);
        x2 = x2 - 50 * Math.cos(angle);
        y2 = y2 - 50 * Math.sin(angle);

        const arrow = new Konva.Arrow({
          points: [
            x1, y1,
            x2, y2
          ],
          pointerLength: 10,
          pointerWidth: 10,
          fill: 'black',
          stroke: 'black',
          strokeWidth: 3
        });

        this.layer.add(arrow);
        this.layer.draw();
        this.arrows.push([this.selectedSource.children[1].text(), this.selectedDestination.children[1].text()]);

        this.selectedSource = null;
        this.selectedDestination = null;
      }
    },
    play() {
      for (let queue of this.queues) {
        if (this.arrows.some(arrow => arrow[1] === queue.children[1].text() || arrow[0] === queue.children[1].text())) {
          continue;
        } else {
          alert(queue.children[1].text() + " is not connected!");
          return;
        }
      }
      for (let machine of this.machines) {
        if (this.arrows.some(arrow => arrow[0] === machine.children[1].text() || arrow[1] === machine.children[1].text())) {
          continue;
        } else {
          alert(machine.children[1].text() + " is not connected!");
          return;
        }
      }
      for (let arrow of this.arrows) {
        let tmp = arrow[0] + '>' + arrow[1];
        this.arrowsGroup.push(tmp);

      }
      console.log("arrows group")
      console.log(this.arrowsGroup)
      for(let q of this.queues){
        q.children[2].text('0')
      }
      fetch(`http://localhost:8080/start?machines=${this.machines.length}&queues=${this.queues.length}
             &arrows=${this.arrowsGroup}&numberofProducts=${this.NumberofProducts}`, {
        method: 'POST',
      })
        .then(res => res.json())
        .then(data => {
          console.log(data)
          console.log("Machines and Queues and arrows send successfully")
        })
        .catch(error => {
          console.error('Error:', error)
        });
    },
    replay() {
      console.log("replay")
      for(let q of this.queues){
        q.children[2].text('0')
      }
      fetch(`http://localhost:8080/replay`, {
        method: 'POST',
      })
    }
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
</style>
