
import {Action} from './action.js';
import {Scene} from './scene.js';
/* eslint-disable no-invalid-this */
window.addEventListener('load', () => {

	window.scene = new Scene();
	// Set Google Assistant Canvas Action at scene level
	window.scene.action = new Action(scene);
	// Call setCallbacks to register interactive canvas
	window.scene.action.setCallbacks();

	window.canvas = window.interactiveCanvas;

	//AoG 위쪽 상단바 크기 구하기
	const headerheight = async () => {
		return await window.canvas.getHeaderHeightPx();
	};
	console.log(`상단바 크기 : ${headerheight()}`);
});
