$(document).ready(function(){

 load_json_data('country');

 function load_json_data(id, parent_id)
 {
  var html_code = '';
  $.getJSON('location.json', function(data){

   html_code += '<option value="">Select '+id+'</option>';
   $.each(data, function(key, value){
    if(id == 'country')
    {
     if(value.parent_id == 0)
     {
      html_code += '<option value="'+value.name+'">'+value.name+'</option>';
     }
    }
    else
    {
     if(value.parent_id == parent_id)
     {
      html_code += '<option value="'+value.name+'">'+value.name+'</option>';
     }
    }
   });
   $('#'+id).html(html_code);
  });

 }

 $(document).on('change', '#country', function(){
  var country_id = $(this).val();
  if(country_id != '')
  {
   load_json_data('state', country_id);
  }
  else
  {
   $('#state').html('<option value="">管轄エリアを選択</option>');
   $('#city').html('<option value="">登録法務局を選択</option>');
  }
 });
 $(document).on('change', '#state', function(){
  var state_id = $(this).val();
  if(state_id != '')
  {
   load_json_data('city', state_id);
  }
  else
  {
   $('#city').html('<option value="">登録法務局を選択</option>');
  }
 });
});