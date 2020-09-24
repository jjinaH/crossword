export class Common{

    /**
     * 좌측 사용자 레벨, 경험치
     * @type {HTMLDivElement}
     */
    #levelBox;
    #userLevel;
    #userMail;
    #userExpBox;
    #userExp;
    #userExpText;
    /**
     * 우측 상단에
     * 힌트와 코인
     * @type {HTMLDivElement}
     */
    #hint_coinBox;
    #hintBox;
    #hintIcon;
    #hintText;
    //#hintStoreButton;
    #coinBox;
    #coinIcon;
    #coinText;
    //#coinStoreButton;
    /**
     * 우측 하단에
     * 메인, 랭킹, 설정
     * @type {HTMLDivElement}
     */
    #bottomCommon;
    #mainButton;
    #rankingButton;
    #settingButton;

    constructor(container) {
        this.container = container;
        console.log("Common Constructor");
    }//constructor

    doDisplay(){
        console.log("Common DoDisplay");
        const pages=document.querySelectorAll("#hint_coinBox,#levelBox,#bottomCommon");
        for (let page of pages){
            if(page.style.display =='none') {
                page.style.display = 'flex';
            }else{
                page.style.display = 'none';
            }
        }
    }

    init(){
        console.log("Common init()");
        /**
         * 좌측 사용자 레벨, 경험치
         * @type {HTMLDivElement}
         */
        this.#levelBox = document.createElement("div");
        this.#levelBox.setAttribute("id", "levelBox");
        this.container.appendChild(this.#levelBox);

        this.#userLevel = document.createElement("div");
        this.#userLevel.setAttribute("id", "userLevel");
        //this.#userLevel.textContent = "Lv." + level;
        this.#levelBox.appendChild(this.#userLevel);

        this.#userMail = document.createElement("div");
        this.#userMail.setAttribute("id", "userMail");
        //userMail.textContent = userEmail;
        this.#levelBox.appendChild(this.#userMail);

        this.#userExpBox = document.createElement("div");
        this.#levelBox.appendChild(this.#userExpBox);

        this.#userExp = document.createElement("progress");
        this.#userExp.setAttribute("id", "progress");
        //this.#userExp.setAttribute("value", exp);
        //this.#userExp.setAttribute("max", fullExp);
        this.#userExpBox.appendChild(this.#userExp);

        this.#userExpText = document.createElement("div");
        this.#userExpText.setAttribute("id", "userExpText");
        //this.#userExpText.textContent = exp + "/" + fullExp;
        this.#userExpBox.appendChild(this.#userExpText);

        /**
         * 우측 상단에
         * 힌트와 코인
         * @type {HTMLDivElement}
         */
        this.#hint_coinBox = document.createElement("div");
        this.#hint_coinBox.setAttribute("id", "hint_coinBox");
        this.container.appendChild(this.#hint_coinBox);

        this.#hintBox = document.createElement("div");
        this.#hintBox.setAttribute("id", "hintBox");
        this.#hintIcon = document.createElement("i");
        this.#hintIcon.setAttribute("class", "fa fa-neuter");
        this.#hintIcon.setAttribute("id", "hintIcon");
        //this.#hintIcon.onclick = shop;
        this.#hintBox.appendChild(this.#hintIcon);
        this.#hint_coinBox.appendChild(this.#hintBox);

        this.#hintText = document.createElement("div");
        this.#hintText.setAttribute("id", "hintText");
        //this.#hintText.textContent = myHint;
        this.#hintBox.appendChild(this.#hintText);

        this.#coinBox = document.createElement("div");
        this.#coinBox.setAttribute("id", "coinBox");
        this.#coinIcon = document.createElement("i");
        this.#coinIcon.setAttribute("class", "fa fa-eur");
        this.#coinIcon.setAttribute("id", "coinIcon");
        //this.#coinIcon.onclick = shop;
        this.#coinBox.appendChild(this.#coinIcon);
        this.#hint_coinBox.appendChild(this.#coinBox);

        this.#coinText = document.createElement("div");
        this.#coinText.setAttribute("id", "coinText");
        //this.#coinText.textContent = myCoin;
        this.#coinBox.appendChild(this.#coinText);

        /**
         * 우측 하단에
         * 메인, 랭킹, 설정
         * @type {HTMLDivElement}
         */
        this.#bottomCommon = document.createElement("div");
        this.#bottomCommon.setAttribute("id", "bottomCommon");
        this.container.appendChild(this.#bottomCommon);

        this.#mainButton = document.createElement("i");
        this.#mainButton.setAttribute("class", "fa fa-home");
        this.#mainButton.setAttribute("id", "main");
        //this.#mainButton.onclick = home;
        this.#bottomCommon.appendChild(this.#mainButton);

        // const welcomeback = document.createElement("i");
        // welcomeback.setAttribute("class", "fa fa-reply");
        // welcomeback.setAttribute("id", "welcomeback");
        // bottomCommon.appendChild(welcomeback);

        this.#rankingButton = document.createElement("i");
        this.#rankingButton.setAttribute("class", "fa fa-star");
        this.#rankingButton.setAttribute("id", "ranking");
        //this.#rankingButton.onclick = ranking;
        this.#bottomCommon.appendChild(this.#rankingButton);

        this.#settingButton = document.createElement("i");
        this.#settingButton.setAttribute("class", "fa fa-cog");
        this.#settingButton.setAttribute("id", "setting");
        //this.#settingButton.onclick = setting;
        this.#bottomCommon.appendChild(this.#settingButton);

    }

    /**
     * @Function Getter
     */
    get levelBox() {
        return this.#levelBox;
    }

    get userLevel() {
        return this.#userLevel;
    }

    get userMail() {
        return this.#userMail;
    }

    get userExpBox() {
        return this.#userExpBox;
    }

    get userExp() {
        return this.#userExp;
    }

    get userExpText() {
        return this.#userExpText;
    }

    get hint_coinBox() {
        return this.#hint_coinBox;
    }

    get hintBox() {
        return this.#hintBox;
    }

    get hintIcon() {
        return this.#hintIcon;
    }

    get hintText() {
        return this.#hintText;
    }

    get coinBox() {
        return this.#coinBox;
    }

    get coinIcon() {
        return this.#coinIcon;
    }

    get coinText() {
        return this.#coinText;
    }

    get bottomCommon() {
        return this.#bottomCommon;
    }

    get mainButton() {
        return this.#mainButton;
    }

    get rankingButton() {
        return this.#rankingButton;
    }

    get settingButton() {
        return this.#settingButton;
    }

}//class Common
