class ClassWithPrivateField {

    #privateField
    _privateField
    constructor() {
        this.#privateField = 42
        this._privateField = 54
       // this.#randomField = 666 // Syntax error
    }
    getPrivatefield(){
        return this.#privateField
    }
    getPrivatefield2(){
        return this._privateField
    }
}

const instance = new ClassWithPrivateField()
console.log(instance.getPrivatefield())// Syntax error