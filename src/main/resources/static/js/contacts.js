console.log("contacts.js");
const baseURL = "http://localhost:8081";
const viewContactModal=document.getElementById('view_contact_modal');
const options = {
    placement: 'bottom-right',
    backdrop: 'dynamic',
    backdropClasses:
        'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
    closable: true,
    onHide: () => {
        console.log('modal is hidden');
    },
    onShow: () => {
        console.log('modal is shown');
    },
    onToggle: () => {
        console.log('modal has been toggled');
    },
};

const instanceOptions = {
    id: 'view-contact_modal',
    override: true
  };

const contactModal = new Modal(viewContactModal, options, instanceOptions)

function openContactModal(){
    contactModal.show();
}

function closeContactModal(){
    contactModal.hide();
}

async function loadContactData(id){
    console.log(id);
    try {
        const data = await (await fetch(`${baseURL}/api/contacts/${id}`)).json();
        console.log(data);
        document.querySelector("#contact_name").innerHTML = data.name;
        document.querySelector("#contact_email").innerHTML = data.email;
        document.querySelector("#contact_image").src = data.contactImage;
        document.querySelector("#contact_address").innerHTML = data.address;
        document.querySelector("#contact_phone").innerHTML = data.phoneNumber;
        document.querySelector("#contact_about").innerHTML = data.description;
        const contactFavorite = document.querySelector("#contact_favorite");
        if (data.favorite) {
          contactFavorite.innerHTML =
          "<i class='fa-solid fa-heart' style='color: #ff2600;'></i>";
        } else {
          contactFavorite.innerHTML = "<i class='fa-solid fa-heart-crack'></i>";
        }
    
        // document.querySelector("#contact_website").href = data.instaLink;
        // document.querySelector("#contact_website").innerHTML = data.instaLink;
        // document.querySelector("#contact_linkedIn").href = data.linkedInLink;
        // document.querySelector("#contact_linkedIn").innerHTML = data.linkedInLink;
        const insta = document.querySelector("#contact_website");
        if ( data.instaLink != "") {
            insta.href =
            data.instaLink
          } else {
            insta.href = "#";
          }

        if ( data.instaLink != "") {
            insta.innerHTML = data.instaLink;
          } else {insta.innerHTML=
            "<i class='fa-solid fa-notdef'></i>";
          }

          const linkedIn = document.querySelector("#contact_linkedIn");
          if (data.linkedInLink != "") {
              linkedIn.href =  data.linkedInLink;
            } else {
              linkedIn.href = "#";
            }

          if (data.linkedInLink != "") {
                
                console.log(typeof(data.linkedInLink));
              linkedIn.innerHTML = data.linkedInLink;
            } else {
                linkedIn.innerHTML=
              "<i class='fa-solid fa-notdef'></i>";
            }
        openContactModal();
      } catch (error) {
        console.log("Error:",error);
    }
    
}

//delete contact
async function deleteContact(id){
    Swal.fire({
        title: "Are you sure?",
        text: "You won't be able to revert this!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes, delete it!"
      }).then((result) => {
        if (result.isConfirmed) {
            const url=`${baseURL}/user/contacts/delete/${id}`;
            window.location.replace(url);
          Swal.fire({
            title: "Deleted!",
            text: "Your file has been deleted.",
            icon: "success"
          });
        }
      });
}