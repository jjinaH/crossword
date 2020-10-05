export class Setting{

    #_settingBox
    #_leftBox
    #_rightBox
    #_soundTextBox
    #_soundButtonBox
    #_resetTextBox
    #_resetButtonBox
    #_soundEffectText
    #_backgroundSoundText
    #_userAccountBox
    #_accountBox
    #_accountCircle
    #_accountIcon
    #_accountText
    #_gameResetText
    #_soundEffectButton
    #_backgroundSoundButton
    #_gameResetButton
    #_gameResetCircle
    #_resetText
    #_resetIcon
    #_effectSound
    #_effectLabel
    #_effectSpan
    #_bgSound

    constructor(container) {
        this.container = container;
        console.log("Setting Constructor");
    }

    doDisplay() {
        console.log("Setting DoDisplay");
        const page = document.querySelector("#settingBox");
        if(page.style.display == 'none')
            page.style.display = 'flex';
        else page.style.display = 'none';
    }

    init() {

        this.#_settingBox = document.createElement("div");
        this.#_settingBox.setAttribute("id", "settingBox");
        this.container.appendChild(this.#_settingBox);

        this.#_leftBox = document.createElement("div");
        this.#_settingBox.appendChild(this.#_leftBox);

        this.#_rightBox = document.createElement("div");
        this.#_settingBox.appendChild(this.#_rightBox);

        this.#_soundTextBox = document.createElement("div");
        this.#_leftBox.appendChild(this.#_soundTextBox);

        this.#_soundButtonBox = document.createElement("div");
        this.#_rightBox.appendChild(this.#_soundButtonBox);

        this.#_soundEffectText = document.createElement("div");
        this.#_soundEffectText.setAttribute("class", "font settingText");
        this.#_soundEffectText.textContent = "Sound Effect";
        this.#_soundTextBox.appendChild(this.#_soundEffectText);

        this.#_backgroundSoundText = document.createElement("div");
        this.#_backgroundSoundText.setAttribute("class", "font settingText");
        this.#_backgroundSoundText.textContent = "Background Sound";
        this.#_soundTextBox.appendChild(this.#_backgroundSoundText);

        this.#_soundEffectButton = document.createElement("div");
        this.#_soundButtonBox.appendChild(this.#_soundEffectButton);

        this.#_backgroundSoundButton = document.createElement("div");
        this.#_soundButtonBox.appendChild(this.#_backgroundSoundButton);

        //on-off button 생성
        this.#_effectSound = document.createElement("input");
        this.#_effectSound.setAttribute("type", "checkbox");
        this.#_soundEffectButton.appendChild(this.#_effectSound);

        this.#_effectLabel = document.createElement("label");
        this.#_effectLabel.setAttribute("for", "checkbox");
        this.#_effectLabel.setAttribute("id", "effectSound");
        this.#_soundEffectButton.appendChild(this.#_effectLabel);

        this.#_effectSpan = document.createElement("span");
        this.#_effectLabel.appendChild(this.#_effectSpan);



        this.#_resetTextBox = document.createElement("div");
        this.#_leftBox.appendChild(this.#_resetTextBox);

        this.#_resetButtonBox = document.createElement("div");
        this.#_rightBox.appendChild(this.#_resetButtonBox);

        this.#_userAccountBox = document.createElement("div");
        this.#_userAccountBox.setAttribute("class", "resetPadding flex");
        this.#_resetTextBox.appendChild(this.#_userAccountBox);

        this.#_accountBox = document.createElement("div");
        this.#_accountBox.setAttribute("class", "accountPadding");
        this.#_userAccountBox.appendChild(this.#_accountBox);

        this.#_accountCircle = document.createElement("div");
        this.#_accountCircle.setAttribute("class", "circle blue settingCircle");
        this.#_accountBox.appendChild(this.#_accountCircle);

        this.#_accountIcon = document.createElement("img");
        this.#_accountIcon.setAttribute("src", "../image/setting/account_icon.png");
        this.#_accountIcon.setAttribute("id", "accountIcon");
        this.#_accountCircle.appendChild(this.#_accountIcon);

        this.#_accountText = document.createElement("span");
        this.#_accountText.setAttribute("class", "settingSmallText");
        this.#_accountText.textContent = "o2o@o2o.kr";
        this.#_userAccountBox.appendChild(this.#_accountText);

        this.#_gameResetText = document.createElement("span");
        this.#_gameResetText.setAttribute("class", "font settingText");
        this.#_gameResetText.textContent = "GAME RESET";
        this.#_resetTextBox.appendChild(this.#_gameResetText);

        this.#_gameResetButton = document.createElement("div");
        this.#_resetButtonBox.appendChild(this.#_gameResetButton);

        this.#_gameResetCircle = document.createElement("div");
        this.#_gameResetCircle.setAttribute("class", "blue settingRound boxShadow settingButton center")
        this.#_gameResetButton.appendChild(this.#_gameResetCircle);

        this.#_resetText = document.createElement("span");
        this.#_resetText.setAttribute("class", "font resetText");
        this.#_resetText.textContent = "RESET";
        this.#_gameResetCircle.appendChild(this.#_resetText);

        this.#_resetIcon = document.createElement("img");
        this.#_resetIcon.setAttribute("src", "../image/setting/reset_icon.png");
        this.#_resetIcon.setAttribute("id", "resetIcon");
        this.#_gameResetCircle.appendChild(this.#_resetIcon);

    }

    get settingBox() {
        return this.#_settingBox;
    }


}