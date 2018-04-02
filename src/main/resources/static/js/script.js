function genPDF()
  {
   html2canvas(document.getElementById("test"),{
   onrendered:function(canvas){

   var img=canvas.toDataURL("image/png");
   var doc = new jsPDF();
   doc.addImage(img,'JPEG',20,20);
   doc.save('test.pdf');
   }

   });

  }