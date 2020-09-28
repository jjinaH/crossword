export class MainFrame {

    /**
     * 중앙에 이어하기, 단계 선택 버튼
     * @type {HTMLDivElement}
     */
    #_playBox
    #_gameText
    #_continueText
    #_gameContinueBox
    #_gameContinueIcon
    #_chooseText
    #_chooseLevelText
    #_chooseLevelBox
    #_chooseLevelIcon

    constructor(container) {
        this.container = container;
        console.log("MainFrame Constructor");
    }//constructor

    doDisplay() {
        console.log("MainFrame DoDisplay");
        const page = document.querySelector("#playBox");
        if (page.style.display == 'none') {
            page.style.display = 'flex';
        } else {
            page.style.display = 'none';
        }

    }

    init() {
        /**
         * 중앙에 이어하기, 단계 선택 버튼
         * @type {HTMLDivElement}
         */

        this.#_playBox = document.createElement("div");
        this.#_playBox.setAttribute("id", "playBox");
        this.container.appendChild(this.#_playBox);

        this.#_gameText = document.createElement("span");
        this.#_gameText.setAttribute("id", "gameText");
        this.#_gameText.textContent = "GAME";
        this.#_playBox.appendChild(this.#_gameText);

        this.#_continueText = document.createElement("span");
        this.#_continueText.setAttribute("id", "continueText");
        this.#_continueText.textContent = "CONTINUE";
        this.#_playBox.appendChild(this.#_continueText);

        this.#_gameContinueBox = document.createElement("div");
        this.#_gameContinueBox.setAttribute("id", "gameContinueBox");
        this.#_playBox.appendChild(this.#_gameContinueBox);

        this.#_gameContinueIcon = document.createElement("img");
        this.#_gameContinueIcon.setAttribute("id", "gameContinueIcon");
        this.#_gameContinueIcon.setAttribute("src", "../image/mainFrame/continue_icon.png");
        this.#_gameContinueBox.appendChild(this.#_gameContinueIcon);


        this.#_chooseText = document.createElement("span");
        this.#_chooseText.setAttribute("id", "chooseText");
        this.#_chooseText.textContent = "CHOOSE";
        this.#_playBox.appendChild(this.#_chooseText);

        this.#_chooseLevelText = document.createElement("span");
        this.#_chooseLevelText.setAttribute("id", "chooseLevelText");
        this.#_chooseLevelText.textContent = "LEVEL";
        this.#_playBox.appendChild(this.#_chooseLevelText);

        this.#_chooseLevelBox = document.createElement("div");
        this.#_chooseLevelBox.setAttribute("id", "chooseLevelBox");
        this.#_playBox.appendChild(this.#_chooseLevelBox);

        this.#_chooseLevelIcon = document.createElement("img");
        this.#_chooseLevelIcon.setAttribute("id", "chooseLevelIcon");
        this.#_chooseLevelIcon.setAttribute("src", "../image/mainFrame/chooseLv_icon.png");
        this.#_chooseLevelBox.appendChild(this.#_chooseLevelIcon);
    }

    get gameContinueBox() {
        return this.#_gameContinueBox;
    }

    get gameContinueIcon() {
        return this.#_gameContinueIcon;
    }

    get chooseLevelBox() {
        return this.#_chooseLevelBox;
    }

    get chooseLevelIcon() {
        return this.#_chooseLevelIcon;
    }
}//class MainFrame
