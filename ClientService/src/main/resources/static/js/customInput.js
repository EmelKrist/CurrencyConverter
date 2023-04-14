$('body').on('input', '.input-number', function(){
    // удаляем все символы, что не являются числами
    this.value = this.value.replace(/[^0-9]/g, '');
});