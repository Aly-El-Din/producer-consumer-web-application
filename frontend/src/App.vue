<template>
  <div id="app">
    <div ref="stage" id="container"></div>
    <ToolBar @addMachine="addMachine" @addQueue="addQueue" @addArrow="addArrow" />
  </div>
</template>

<script>
import ToolBar from './components/ToolBar.vue'
import Konva from 'konva'

export default {
  name: 'App',
  components: {
    ToolBar
  },
  data() {
    return {
      stage: null,
      layer: null,
      machines: [],
      queues: [],
      arrows: [],
      nextMachineId: 1,
      nextQueueId: 0,
    }
  },
  mounted() {
    this.stage = new Konva.Stage({
      container: 'container',
      width: 1510,
      height: 675
    });
    this.layer = new Konva.Layer();
    this.stage.add(this.layer);
  },
  methods: {
    createShapeWithText(xc, yc, shape, textString, id) {
      const text = new Konva.Text({
        x: xc,
        y: yc,
        text: textString,
        fontSize: 20,
        fill: 'black',
      });

      const group = new Konva.Group({
        draggable: true,
        id: id,
      });

      group.add(shape);
      group.add(text);

      return group;
    },

    addMachine() {
      const machine = new Konva.Circle({
        x: 42,
        y: 42,
        radius: 40,
        fill: 'grey',
        stroke: 'black',
      });

      const machineGroup = this.createShapeWithText(30, 34, machine, 'M' + this.nextMachineId);

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
        fill: 'grey',
        stroke: 'black',
      });

      const queueGroup = this.createShapeWithText(40, 10, queue, 'Q' + this.nextQueueId);

      this.layer.add(queueGroup);
      this.queues.push(queueGroup);
      this.layer.draw();
      this.nextQueueId++;
    },
    findClickedShape() {
      const pointer = this.stage.getPointerPosition();
      console.log(this.stage.getPointerPosition())
      // Iterate through machines and check if the click occurred within a machine
      for (const machine of this.machines) {
        const shape = machine.findOne('Circle');
        if (this.isClickInsideShape(pointer, shape)) {
          console.log("here1")
          return shape;
        }
      }

      // Iterate through queues and check if the click occurred within a queue
      for (const queue of this.queues) {
        const shape = queue.findOne('Rect');
        if (this.isClickInsideShape(pointer, shape)) {
          return shape;
        }
      }

      // If no shape is found, return null
      return null;
    },

    isClickInsideShape(pointer, shape) {
      // Check if the click occurred within the boundaries of the shape
      return (
        pointer.x >= shape.x() &&
        pointer.x <= shape.x() + shape.width() &&
        pointer.y >= shape.y() &&
        pointer.y <= shape.y() + shape.height()
      );
    }
    ,
    addArrow() {
      let startShape = null;

      // Listen for the first click to set the starting point
      const startListener = () => {
        startShape = this.findClickedShape();
        if (startShape) {
          this.stage.off('click', startListener); // Remove the listener after the first click
          this.stage.on('click', endListener); // Add a new listener for the second click
        }
      };

      // Listen for the second click to set the ending point
      const endListener = () => {
        const endShape = this.findClickedShape();
        if (endShape && startShape !== endShape) {
          // Create the arrow based on the start and end points
          const arrow = new Konva.Arrow({
            points: [startShape.x(), startShape.y(), endShape.x(), endShape.y()],
            pointerLength: 10,
            pointerWidth: 10,
            fill: 'black',
            stroke: 'black',
            strokeWidth: 4,
          });

          const arrowGroup = new Konva.Group({
            draggable: true,
            id: 'arrow' + Date.now(),
          });

          arrowGroup.add(arrow);

          this.layer.add(arrowGroup);
          this.arrows.push(arrowGroup);
          this.layer.draw();

          // Remove the listener after the second click
          this.stage.off('click', endListener);
        } else {
          // Handle the case where the second click is on the same shape as the first click
          this.stage.off('click', endListener);
        }
      };

      // Start listening for the first click
      this.stage.on('click', startListener);
    },

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
