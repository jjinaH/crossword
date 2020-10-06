export class StageSelect{
    #stepBox;
    #stepButton;

    doDisplay(){
        console.log("stageSelect DoDisplay");
        const page=document.querySelector("#stepBox");
            if(page.style.display =='none') {
                page.style.display = 'flex';
            }else{
                page.style.display = 'none';
            }
    }//doDisplay

    constructor(container) {
        this.container = container;
        console.log("StageSelect Constructor");
    }//constructor

    init(){
        this.#stepBox = document.createElement("div");
        this.#stepBox.setAttribute("id", "stepBox");
        this.container.appendChild(this.#stepBox);
        this.#stepButton = document.createElement("div");
        this.#stepButton.setAttribute("id", "stepButton");
        this.#stepBox.appendChild(this.#stepButton);

    }
    stepLock(step) {
        const totalStep = 10;
        if (step > 12) {
            const nextPage = document.createElement("div");
            nextPage.setAttribute("id", "nextPage");
            const nextIcon = document.createElement("i");
            nextIcon.setAttribute("class", "fa fa-angle-right fa-4x");
            nextPage.appendChild(nextIcon);
            this.#stepButton.appendChild(nextPage);
        }
        let stepNum = 0;
        for (let row = 0; row < 3; row++) {
            const stepRow = document.createElement("div");
            stepRow.setAttribute("id", "stepRow");
            this.#stepButton.appendChild(stepRow);
            for (let col = 0; col < 4; col++) {
                if (stepNum == totalStep) return;
                stepNum++;
                const underBox = document.createElement("div");
                if (step < stepNum) { //disable
                    const outerBox = document.createElement("div");
                    outerBox.setAttribute("id", "outerBox");
                    stepRow.appendChild(outerBox);
                    underBox.textContent = "Lv. " + stepNum;
                    underBox.setAttribute("class", "disableUnderBox");
                    const upperBox = document.createElement("div");
                    upperBox.setAttribute("class", "upperBox");
                    upperBox.textContent = "Lock";
                    outerBox.appendChild(underBox);
                    outerBox.appendChild(upperBox);
                } else {
                    underBox.textContent = "Lv. " + stepNum;
                    const num = stepNum;
                    console.log("open : " + num);
                    underBox.setAttribute("class", "ableUnderBox");
                    underBox.addEventListener("click", function () {
                        window.canvas.sendTextQuery("level" + num);
                    });
                    stepRow.appendChild(underBox);
                }
            }
        }
    }

}