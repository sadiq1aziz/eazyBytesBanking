package com.bankEasy.accounts.controller;


import com.bankEasy.accounts.dto.CustomerDto;
import com.bankEasy.accounts.dto.ResponseDto;
import com.bankEasy.accounts.constants.AccountsConstants;
import com.bankEasy.accounts.service.IAccountsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


// controllers here will simply handle request and responses, validation etc. No business logic here
@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
// To ensure validated input in request
@Validated
//tag to document summary for Controller/ classes
@Tag(
        name = "Controller for Accounts in EazyBank",
        description = "Process CRUD operations for CREATE, UPDATE, DELETE, READ for Accounts"
)
public class AccountsController {


    private IAccountsService iAccountsService;

    //tag to document summary for API methods
    @Operation (
            summary = "Account Creation",
            description = "create an Account in EazyBank"
    )
    //tag for documenting the operation type / api response
    @ApiResponse(
            responseCode = "201",
            description = "HTTP Status created"
    )
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createAccount (@Valid @RequestBody CustomerDto customerDto){

        //invoke service for business logic
        iAccountsService.createAccount(customerDto);

        //status method allows us to return in the response header that the account has been
        //created successfully via 201 code
        //we also return the same as pertains to the response dto
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201));
    }


    @GetMapping("/fetch")
    public ResponseEntity<CustomerDto> fetchAccount ( @RequestParam  @Pattern(regexp = "(^$|[0-9]{10})", message = "valid customer mobile number required") String mobileNumber){

        //invoke service for business logic
        CustomerDto customerDto = iAccountsService.fetchAccount(mobileNumber);


        return ResponseEntity
                .status(HttpStatus.OK)
                .body(customerDto);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateAccount ( @Valid @RequestBody  CustomerDto customerDto) {

        boolean isUpdated = iAccountsService.updateAccount(customerDto);
        if (isUpdated){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto(AccountsConstants.MESSAGE_417_UPDATE, "kindly Contact Customer service"));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteAccount(@RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "valid customer mobile number required") String mobileNumber){

       boolean isDeleted = iAccountsService.deleteAccount(mobileNumber);
       if (isDeleted){
           return ResponseEntity
                   .status(HttpStatus.OK)
                   .body(new ResponseDto(AccountsConstants.STATUS_200, "Account Deleted Successfully"));
       } else {
           return ResponseEntity
                   .status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body(new ResponseDto(AccountsConstants.STATUS_417, AccountsConstants.MESSAGE_417_DELETE));
       }

    }
}
