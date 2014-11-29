$(document).ready(function() {
	
	initValidacao();
});

/**
 * Carrega as validações para os campos marcados com a respectiva validação
 * assinalada no styleClass (class).
 */

function initValidacao(){
//	adicionarFormValidator();
	adicionarNumeric();
	//setarFocus();
	adicionarMonetarioBr();
	adicionarPesoBr();
	adicionarCPF();
	adicionarCNPJ();
	adicionarMaskDate();
	//adicionarValidacaoData();
	
//	adicionaValidacaoInteger();
	adicionaInteger();
//	adicionarValidacaoCpf();
//	adicionaValidacaoRangeMonetario();
//	adicionarValidacaoCep();
//	adcionarValueOptionSelecione();	
	formataDecimalDuasCasas();
}

function adicionaInteger(){
	jQuery('.integer').integer();
}

/**
 * Pinta o elemento que possui a classe .containerErro que contém elementos errados de vermelho.
 * @param elementId
 * @return
 */
function pintaErroAba(elementId) {
	var tabs = jQuery('.containerErro');
	
	for (i=0;i<tabs.length;i++) {
		var tab = tabs[i];
		var tabBodyId = '' + tab.id;

		tabBodyId = tabBodyId.substring(0, tabBodyId.lastIndexOf("_"));
		if (tab.id && jQuery('*[id='+tabBodyId+'] *[id$='+elementId+'][class*=error],*[id='+tabBodyId+'] *[id$='+elementId+'] .error').length > 0) {
			jQuery('*[id=' + tab.id + ']').addClass('error');
		}
	}
}

/**
 * Função para adicionar evento onclick da mensagem gritter utilizada pelo sistema.
 * @return
 */
function addOnClickGritter(){
	jQuery('*[id^=gritter-item-]').unbind('click');
	jQuery('*[id^=gritter-item-]').bind('click', function(e){
	    $(this.id).remove();    
	});
}

function limpaFormValidationErrors() {
	jQuery('.error').removeClass('error');
}

/**
 * Função para validação do form do lado do cliente
 */
function adicionarFormValidator() {
	
	jQuery('formI').validate({meta: "validate", errorPlacement: function(error, element) {
		var elementId = jQuery.trim(element.attr('id'));
		if (null == elementId || "" == elementId) {
			elementId = jQuery.trim(element.attr('name'));
		}

		elementId = elementId.substring(elementId.indexOf(':') + 1);
		try{
			jQuery('span[id$=' + elementId + '_erro]')[0].innerHTML='';//limpando message		
		}catch (e) {}
			
		
		error.appendTo(jQuery('span[id$=' + elementId + '_erro]')[0]);

		pintaErroAba(elementId);
		jQuery('*[id$=btnSalvar]').bind('click', function(e) {pintaErroAba(elementId);});
	}});
}
 
 function adicionarFormValidatorPopUp() {
	 
		jQuery('form[id$=:formAtributo]').validate({meta: "validate", errorPlacement: function(error, element) {
			var elementId = jQuery.trim(element.attr('id'));
			if (null == elementId || "" == elementId) {
				elementId = jQuery.trim(element.attr('name'));
			}

			elementId = elementId.substring(elementId.indexOf(':') + 1);
			try{
				jQuery('span[id$=' + elementId + '_erro]')[0].innerHTML='';//limpando message		
			}catch (e) {}
				
			
			error.appendTo(jQuery('span[id$=' + elementId + '_erro]')[0]);

			pintaErroAba(elementId);
			jQuery('*[id$=btnSalvar]').bind('click', function(e) {pintaErroAba(elementId);});
		}});
		
		
	}
 
function adicionarFormValidatorPopUpEdicao() {
	
	jQuery('form[id$=:formAtributoEdicao]').validate({meta: "validate", errorPlacement: function(error, element) {
			var elementId = jQuery.trim(element.attr('id'));
			if (null == elementId || "" == elementId) {
				elementId = jQuery.trim(element.attr('name'));
			}
			
			elementId = elementId.substring(elementId.indexOf(':') + 1);
			try{
				jQuery('span[id$=' + elementId + '_erro]')[0].innerHTML='';//limpando message		
			}catch (e) {}
				
			
			error.appendTo(jQuery('span[id$=' + elementId + '_erro]')[0]);

			pintaErroAba(elementId);
			jQuery('*[id$=btnSalvar]').bind('click', function(e) {pintaErroAba(elementId);});			

		}});
				
	}
 
 
 function adicionarFormValidatorPopUpReprovacao() {
	 
		jQuery('form#reprovacao').validate({meta: "validate", errorPlacement: function(error, element) {
			var elementId = jQuery.trim(element.attr('id'));
			
			if (null == elementId || "" == elementId) {
				elementId = jQuery.trim(element.attr('name'));
			}

			elementId = elementId.substring(elementId.indexOf(':') + 1);
			try{
				jQuery('span[id$=' + elementId + '_erro]')[0].innerHTML='';//limpando message		
			}catch (e) {}
				
			
			error.appendTo(jQuery('span[id$=' + elementId + '_erro]')[0]);

			pintaErroAba(elementId);
			jQuery('*[id$=btnSalvar]').bind('click', function(e) {pintaErroAba(elementId);});
		}});
		
	}
 
 
 function adicionarFormValidatorPopUpConteudo() {
	 
		jQuery('form[id$=:formPesqConteudo]').validate({meta: "validate", errorPlacement: function(error, element) {
			var elementId = jQuery.trim(element.attr('id'));
			
			if (null == elementId || "" == elementId) {
				elementId = jQuery.trim(element.attr('name'));
			}

			elementId = elementId.substring(elementId.indexOf(':') + 1);
			try{
				jQuery('span[id$=' + elementId + '_erro]')[0].innerHTML='';//limpando message		
			}catch (e) {}
				
			
			error.appendTo(jQuery('span[id$=' + elementId + '_erro]')[0]);

			pintaErroAba(elementId);
			jQuery('*[id$=btnSalvar]').bind('click', function(e) {pintaErroAba(elementId);});
		}});
		
	}
 
 
 function adicionarFormValidatorPopUpAlterarSenha() {
	 
		jQuery('form#formAlterarSenha').validate({meta: "validate", errorPlacement: function(error, element) {
			var elementId = jQuery.trim(element.attr('id'));
			
			if (null == elementId || "" == elementId) {
				elementId = jQuery.trim(element.attr('name'));
			}

			elementId = elementId.substring(elementId.indexOf(':') + 1);
			try{
				jQuery('span[id$=' + elementId + '_erro]')[0].innerHTML='';//limpando message		
			}catch (e) {}
				
			
			error.appendTo(jQuery('span[id$=' + elementId + '_erro]')[0]);

			pintaErroAba(elementId);
			jQuery('*[id$=btnSalvar]').bind('click', function(e) {pintaErroAba(elementId);});
		}});
		
	}
 
//msgs para funcionalidades customizadas no jquery validator
var dataMsg;
var cpfMsg;
/**
 * Adiciona validação de data dentro do validator do jquery.
 */
function adicionarValidacaoData(){
	jQuery.validator.addMethod("date", function(value, element) {

		if(this.optional(element))
			return true;
		if(value=='__/__/____') return true;
		
		if(!value || value.length==0) return true;
		//if(value.length!=10) return false;
		// verificando data
		var data 		= value;
		var dia 		= data.substr(0,2);
		var barra1		= data.substr(2,1);
		var mes 		= data.substr(3,2);
		var barra2		= data.substr(5,1);
		var ano 		= data.substr(6,4);
		if(data.length!=10||barra1!="/"||barra2!="/" //validando mascara, dias e meses
			||isNaN(dia)
			||isNaN(mes)
			||isNaN(ano)
			||dia>31
			||mes>12)return false;
		if((mes==4||mes==6||mes==9||mes==11)&& dia==31)return false;//meses com 30 dias
		if(mes==2 && dia>=29 && ano%4!=0)return false;//ano bisexto
		if(ano < 1900)return false;//delimitando ano máximo

		return true;
	}, dataMsg);  // Mensagem padrão
}

/**
 * Adiciona validação de data dentro do validator do jquery.
 */
function adicionarValidacaoCpf(){
	jQuery.validator.addMethod("cpf", function(value, element) {
		if(this.optional(element))
			return true;
			
		return isCpfValido(value);
			}, cpfMsg);  // Mensagem padrão
}
var cepMsg;
function adicionarValidacaoCep(){
	jQuery.validator.addMethod("cep", function(value, element) {
		if(this.optional(element))
			return true;
		
		if (value.length != 8) {
			return false;
		}
			
		return true;
			}, cepMsg);  // Mensagem padrão
}

function adicionaValidacaoInteger(){
//	jQuery.validator.addMethod("integer", function(value, element) {
//
//		if(this.optional(element))
//			return true;
//		if(!value || value.length==0) return true;
//		return value.search(/[^0-9]/g, "")==-1;
//		return true;
//	}, integerMsg);  // Mensagem padrão
}

function adicionaValidacaoRangeMonetario(){
	jQuery.validator.addMethod("numericRange", function(value, element, params) {
		
		if(this.optional(element))
			return true;
		
		var nValue = value.replace(',', '.');
		return nValue >= params[0] && nValue <= params[1];
	}, jQuery.format(numericRangeMsg));
}

function adicionarMaskDate(){
	//$('.date').mask('99/99/9999',{placeholder:'_'});

	jQuery('.date').mask('99/99/9999');
}

function adicionarNumeric() {
	jQuery('.numeric').numeric();
}

function formataDecimalDuasCasas(){	
	
	jQuery('.decimal').bind('blur', function(){	
		
		this.value = new String(this.value);
		
		if(this.value && this.value.indexOf(',')!=-1){
			this.value= this.value.concat('000');		
			this.value=this.value.substring(0, this.value.indexOf(',')+3);
		}else if(this.value.indexOf(',')==-1){
			this.value=this.value.concat(',000');
		}	
	});
}


/**
 * Função que adiciona máscara monetária em formato pt_br via styleClass="monetario"
 */
function adicionarMonetarioBr(){
	jQuery('.monetario').priceFormat({
		prefix: '',
		centsSeparator: ',',
		thousandsSeparator: '.',
		limit: 14,
        centsLimit: 4
	}); 
}

function adicionarPesoBr(){
	jQuery('.peso').priceFormat({
		prefix: '',
		centsSeparator: ',',
		centsLimit: 3,
		thousandsSeparator: '.'
	}); 
}

/**
 * Função para mascara de cpf via styleClass="cpf"
 */
function adicionarCPF(){
	jQuery('.cpf').mask('999.999.999-99');
}

/**
 * Função para mascara de CNPJ via styleClass="cnpj"
 */
function adicionarCNPJ(){
	jQuery('.cnpj').mask('99.999.999/9999-99');
}


//Esta funcao tenta colocar o foco no primeiro componente que esteja visivel
//e tenha a classe de estilo selecionavel
function setarFocus() {
	try {
		var obj=jQuery('.selecionavel')[0];
		obj.focus();
	} catch(e) {}	
}


function limpar(){
	
	jQuery('.limpavel').each(function() {

		var type = this.type;
		if(type == 'text')
			this.value = '';
		else if(type == 'select-one')
			this.selectedIndex = 0;
		else if(type == 'checkbox')
			this.checked = false;
		else if(type == 'textarea'){
			this.value = '';
		}
	});

	var tab = jQuery('.tabelaPesquisa')[0];
	if(tab != null)
		jQuery('tbody[id=' + tab.id + ':tb]')[0].style.display = "none";

	var comp = jQuery('span[id$=scrollerInfo]')[0];
	if(comp != null)
		comp.innerHTML = "";
}

function verificaTecla(event, idBtn) {
	//alert(navigator.appName);
	//alert('tipo = =' + event.srcElement.nodeName + "=");
	var src ;
	if(event.srcElement){ //IE
		src = event.srcElement.nodeName.toLowerCase();
	} else {  //FF e outros
		src = event.target.type;
	}	
	
	if (src != 'textarea' && src != 'select-one' && src != 'select' && event.keyCode == 13 ) {
		jQuery('input[id$=' + idBtn + ']')[0].click();
		return false;
	}

	return true;
}

function isCpfValido(cpf)
{
var numeros, digitos, soma, i, resultado, digitos_iguais;
digitos_iguais = 1;
if (cpf.length < 11)
      return false;
for (i = 0; i < cpf.length - 1; i++)
      if (cpf.charAt(i) != cpf.charAt(i + 1))
            {
            digitos_iguais = 0;
            break;
            }
if (!digitos_iguais)
      {
      numeros = cpf.substring(0,9);
      digitos = cpf.substring(9);
      soma = 0;
      for (i = 10; i > 1; i--)
            soma += numeros.charAt(10 - i) * i;
      resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
      if (resultado != digitos.charAt(0))
            return false;
      numeros = cpf.substring(0,10);
      soma = 0;
      for (i = 11; i > 1; i--)
            soma += numeros.charAt(11 - i) * i;
      resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
      if (resultado != digitos.charAt(1))
            return false;
      return true;
      }
else
      return false;
}

function msgError(message) {
	jQuery.gritter.add({
		image: contextPath + '/javax.faces.resource/growl/images/error.png.jsf?ln=primefaces&v=3.1.1',
		title: message,
		time: 5000,
		text:''
	});
}

function msgInfo(message) {
	jQuery.gritter.add({
		image: contextPath + '/javax.faces.resource/growl/images/info.png.jsf?ln=primefaces&v=3.1.1',
		title: message,
		time: 5000,
		text:''
	});
}

/**
 * @param 
 * @return
 */
function instanceJSGrow(message, severity){
		if(message){
			if(severity!=0){
				msgError(message);
			}else{
				msgInfo(message);
			}
		}		
}

/**
 * Utilizado para o popup de confirmação. 
 * @var A var txtConfirmacao recebe o parametro msg para possibilitar a chamada de um EL via parametro JS.
 * @var A var eventSimConfirmacao recebe uma STRING via parametro. Dentro do onclick do botão sim, localizado
 * no popupConfirmacao.xhtml, essa string é executada como um método (semelhante ao Reflection Java).
 * @param msg Deve ser passado como stirng 'param'. Pode ser com EL com a sintaxe '#{EL}'
 * @param eventSim Deve ser passado como string 'param'.
 * @Exemplo msgConfirmacao('#{bundle.msg}', 'executarExclusao()');
 * @author eugenio.filho
 */
var txtConfirmacao;
var eventSimConfirmacao;
function msgConfirmacao(msg, eventSim){
	txtConfirmacao = msg;
	Richfaces.showModalPanel('popupConfirmacao');
	eventSimConfirmacao = eventSim;
}

function setFocus(idElement){
	try {
		jQuery('[id$='+idElement+']').focus();
	} catch (e) {
		
	}
}

function adcionarValueOptionSelecione(){	
	jQuery('select.comboSelecione').find('option:first').attr('value', '');
}

function validaLimite(form,campo){

	if(jQuery('*[id$='+form+':'+campo+']').attr('value').length > 300){

		 jQuery('*[id$='+form+':'+campo+']').attr('value',jQuery('*[id$='+form+':'+campo+']').attr('value').substring(0,300));
	}
	
}

