(function(){
  function $$(sel, root){
    root = root || document;
    if(typeof sel === 'string') return Array.from(root.querySelectorAll(sel));
    if(sel instanceof Element) return [sel];
    if(Array.isArray(sel)) return sel;
    return [];
  }
  function on(el, ev, fn){ el.addEventListener(ev, fn); }
  function html(el, s){ el.innerHTML = s; }
  function val(el){ return (el.value||'').trim(); }
  function form(selector){
    var root = $$(selector)[0];
    return {
      get: function(){
        var inputs = $$("[name]", root); var o={};
        inputs.forEach(function(e){ o[e.getAttribute('name')] = (e.type==='checkbox'? e.checked : e.value.trim()); });
        return o;
      },
      set: function(obj){ Object.keys(obj).forEach(function(k){ var e=root.querySelector('[name="'+k+'"]'); if(e){ e.value=obj[k]; }}); }
    };
  }
  function ajax(opts){
    var url = opts.url; var method = (opts.method||'GET').toUpperCase();
    var headers = { 'Accept': 'application/json' };
    var body=null;
    if(opts.json){ headers['Content-Type']='application/json'; body = JSON.stringify(opts.data||{}); }
    return fetch(url, { method:method, headers:headers, body:body }).then(function(r){ return r.text(); }).then(function(t){
      try{ return JSON.parse(t); }catch(e){ return t; }
    });
  }
  function template(tpl, data){
    return tpl.replace(/\{\{\s*(\w+)\s*\}\}/g, function(m, k){ var v = data[k]; return v==null?'' : v; });
  }
  function renderList(tpl, list){ return list.map(function(it){ return template(tpl,it); }).join(''); }
  window.$$ = $$(
  );
  window.$ = { on:on, html:html, val:val, ajax:ajax, template:template, renderList:renderList, form:form };
})();