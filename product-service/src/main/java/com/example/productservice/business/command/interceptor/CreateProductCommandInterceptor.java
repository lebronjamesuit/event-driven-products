package com.example.productservice.business.command.interceptor;

import com.example.productservice.business.command.api.CreateProductCommand;
import com.example.productservice.business.command.lookup.ProductLookUpEntity;
import com.example.productservice.business.command.lookup.ProductLookUpRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

@AllArgsConstructor
@Slf4j
@Component
public class CreateProductCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    private final ProductLookUpRepo productLookUpRepo;

    @Nonnull
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>>
             handle(@Nonnull List<? extends CommandMessage<?>> messages) {

        // Input: integer, CommandMessage<?> then Return  CommandMessage<?>

        log.info("CreateProductCommandInterceptor - handle ");

        return  (index , command) -> {
            if(command.getPayload().getClass().equals(CreateProductCommand.class)){
                CreateProductCommand createProductCommand  = (CreateProductCommand) command.getPayload();
                if(createProductCommand.getTitle().startsWith("shit")){
                    throw new IllegalArgumentException("Contain bad language");
                }

                // Check if product is exists
                Optional<ProductLookUpEntity> optionalProductLookUpEntity =
                        productLookUpRepo.findByProductIdOrTitle
                        (createProductCommand.getProductId() ,createProductCommand.getTitle());

                if(optionalProductLookUpEntity.isPresent()){
                    throw new IllegalArgumentException("Product title :" +createProductCommand.getTitle()+
                            " is already exist in database" );
                };

            }
            return command;
        };
    }
}
