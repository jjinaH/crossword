export class Shop{

    #_shopBox;
    #_hintBox;
    #_hintIcon;
    #_hintText;
    #_hintX3Text;
    #_miniCoinIcon1;
    #_buyHintText;
    #_coin300cText;
    #_coinBox;
    #_coinIcon;
    #_coinText;
    #_coin500Text;
    #_miniCoinIcon2;
    #_watchAdsText;
    #_freeText;


    constructor(container) {
        this.container = container;
        console.log("Shop Constructor");
    }

    doDisplay() {
        console.log("Shop DoDisplay");
        const page = document.querySelector("#shopBox");
        if(page.style.display == 'none')
            page.style.display = 'flex';
        else page.style.display = 'none';
    }

    init() {
        this.#_shopBox = document.createElement("div");
        this.#_shopBox.setAttribute("id", "shopBox");
        this.container.appendChild(this.#_shopBox);

        this.#_hintBox = document.createElement("div");
        this.#_hintBox.setAttribute("id", "shopHintBox");
        this.#_shopBox.appendChild(this.#_hintBox);

        this.#_hintIcon = document.createElement("img");
        this.#_hintIcon.setAttribute("id", "shopHintIcon");
        this.#_hintIcon.setAttribute("src", "../image/shop/hint_icon.png");
        this.#_hintBox.appendChild(this.#_hintIcon);

        this.#_hintText = document.createElement("span");
        this.#_hintText.setAttribute("id", "shopHintText");
        this.#_hintText.textContent = "HINT";
        this.#_shopBox.appendChild(this.#_hintText);

        this.#_hintX3Text = document.createElement("span");
        this.#_hintX3Text.setAttribute("id", "shopX3Text");
        this.#_hintX3Text.textContent = "X3";
        this.#_shopBox.appendChild(this.#_hintX3Text);

        this.#_miniCoinIcon1 = document.createElement("img");
        this.#_miniCoinIcon1.setAttribute("class", "miniCoinIcon");
        this.#_miniCoinIcon1.setAttribute("src", "../image/shop/coin_icon.png");
        this.#_shopBox.appendChild(this.#_miniCoinIcon1);

        this.#_buyHintText = document.createElement("span");
        this.#_buyHintText.setAttribute("id", "buyHintText");
        this.#_buyHintText.textContent = "BUY HINT";
        this.#_shopBox.appendChild(this.#_buyHintText);

        this.#_coin300cText = document.createElement("span");
        this.#_coin300cText.setAttribute("id", "coin300cText");
        this.#_coin300cText.textContent = "300c";
        this.#_shopBox.appendChild(this.#_coin300cText);


        this.#_coinBox = document.createElement("div");
        this.#_coinBox.setAttribute("id", "shopCoinBox");
        this.#_shopBox.appendChild(this.#_coinBox);

        this.#_coinIcon = document.createElement("img");
        this.#_coinIcon.setAttribute("id", "shopCoinIcon");
        this.#_coinIcon.setAttribute("src", "../image/shop/coin_icon.png");
        this.#_coinBox.appendChild(this.#_coinIcon);

        this.#_coinText = document.createElement("span");
        this.#_coinText.setAttribute("id", "shopCoinText");
        this.#_coinText.textContent = "COIN";
        this.#_shopBox.appendChild(this.#_coinText);

        this.#_coin500Text = document.createElement("span");
        this.#_coin500Text.setAttribute("id", "shop500Text");
        this.#_coin500Text.textContent = "500";
        this.#_shopBox.appendChild(this.#_coin500Text);

        this.#_miniCoinIcon2 = document.createElement("img");
        this.#_miniCoinIcon2.setAttribute("class", "miniCoinIcon");
        this.#_miniCoinIcon2.setAttribute("src", "../image/shop/coin_icon.png");
        this.#_shopBox.appendChild(this.#_miniCoinIcon2);

        this.#_watchAdsText = document.createElement("span");
        this.#_watchAdsText.setAttribute("id", "watchAdsText");
        this.#_watchAdsText.textContent = "WATCH ADS";
        this.#_shopBox.appendChild(this.#_watchAdsText);

        this.#_freeText = document.createElement("span");
        this.#_freeText.setAttribute("id", "freeText");
        this.#_freeText.textContent = "FREE";
        this.#_shopBox.appendChild(this.#_freeText);


    }

}