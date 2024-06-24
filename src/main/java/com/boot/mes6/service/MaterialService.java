package com.boot.mes6.service;

import com.boot.mes6.constant.MaterialName;
import com.boot.mes6.constant.MaterialStatus;
import com.boot.mes6.constant.MaterialSupplierName;
import com.boot.mes6.constant.ProductName;
import com.boot.mes6.entity.MaterialInOut;
import com.boot.mes6.entity.Order;
import com.boot.mes6.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaterialService {
    private final MaterialRepository materialRepository;

    // 원자재 입출고 이력 가져오기
    public List<MaterialInOut> getAllMaterial() {
        return materialRepository.findAll();
    }

    // 원자재 입출고 이력에 추가하기
    public void addMaterial(MaterialInOut materialInOut) {
        materialRepository.save(materialInOut);
    }

    // 자동 원자재 발주
    // 수주 추가가 되면 추가된 수주의 정보를 Order로 받아옴
    public void autoAddMaterial(Order order) {
        //order에서 orderAmount, orderProductType, orderDate 가져온다.
        //초기값들 설정해야함
        long orderAmount = order.getOrderAmount();
        ProductName orderProductType = order.getOrderProductType();
        LocalDateTime materialOrderDate = order.getOrderDate();
        long materialAmount = 0L;
        long subMaterialAmount = 0L;
        final long MAX_JUICE_ORDER_AMOUNT = 2000L;
        final long MAX_HONEY_ORDER_AMOUNT = 200L;
        final long MAX_STICK_ORDER_AMOUNT = 200L;

        //불량률을 고려해서 생산량 계산
        long productionAmount = (long) (orderAmount + Math.ceil(orderAmount * 0.03));

        //orderProductType이 즙인지 젤리인지
        switch (orderProductType) {
            case CABBAGE_JUICE:
                materialAmount = calculateMaterialAmount(orderProductType, productionAmount);
                subMaterialAmount = calculateSubMaterialAmount(orderProductType, productionAmount);
                calculateReceiptDate(order, materialAmount, 12, MAX_JUICE_ORDER_AMOUNT, MaterialName.CABBAGE, materialOrderDate, MaterialSupplierName.A_FARM);
                calculateReceiptDate(order, subMaterialAmount, 12, MAX_HONEY_ORDER_AMOUNT, MaterialName.HONEY, materialOrderDate, MaterialSupplierName.A_FARM);
                break;
            case GARLIC_JUICE:
                materialAmount = calculateMaterialAmount(orderProductType, productionAmount);
                subMaterialAmount = calculateSubMaterialAmount(orderProductType, productionAmount);
                calculateReceiptDate(order, materialAmount, 12, MAX_JUICE_ORDER_AMOUNT, MaterialName.GARLIC, materialOrderDate, MaterialSupplierName.A_FARM);
                calculateReceiptDate(order, subMaterialAmount, 12, MAX_HONEY_ORDER_AMOUNT, MaterialName.HONEY, materialOrderDate, MaterialSupplierName.A_FARM);
                break;
            case POMEGRANATE_JELLY:
                materialAmount = calculateMaterialAmount(orderProductType, productionAmount);
                subMaterialAmount = calculateSubMaterialAmount(orderProductType, productionAmount);
                calculateReceiptDate(order, materialAmount, 15, MAX_STICK_ORDER_AMOUNT, MaterialName.POMEGRANATE, materialOrderDate, MaterialSupplierName.OO_NH);
                calculateReceiptDate(order, subMaterialAmount, 15, MAX_STICK_ORDER_AMOUNT, MaterialName.COLLAGEN, materialOrderDate, MaterialSupplierName.OO_NH);
                break;
            case PLUM_JELLY:
                materialAmount = calculateMaterialAmount(orderProductType, productionAmount);
                subMaterialAmount = calculateSubMaterialAmount(orderProductType, productionAmount);
                calculateReceiptDate(order, materialAmount, 15, MAX_STICK_ORDER_AMOUNT, MaterialName.PLUM, materialOrderDate, MaterialSupplierName.OO_NH);
                calculateReceiptDate(order, subMaterialAmount, 15, MAX_STICK_ORDER_AMOUNT, MaterialName.COLLAGEN, materialOrderDate, MaterialSupplierName.OO_NH);
                break;
            default:
                throw new IllegalArgumentException("Invalid product type: " + order.getOrderProductType());
        }
    }

    public void calculateReceiptDate(Order order, Long materialAmount, int cutTime, long MAX_ORDER_AMOUNT, MaterialName materialName, LocalDateTime materialOrderDate, MaterialSupplierName supplierName) {
        //수주 정보에서 수주일(발주일)에 따라 기본 발주접수일(materialReceiptDate) 설정
        LocalDateTime materialReceiptDate = null; //기본값

        //orderDate가 금요일 오후, 토요일, 일요일이면 발주접수일을 월요일 12시로 설정
        if (materialOrderDate.getDayOfWeek() == DayOfWeek.FRIDAY && materialOrderDate.getHour() >= cutTime) {
            materialReceiptDate = materialOrderDate.plusDays(3).withHour(cutTime).withMinute(0).withSecond(0).withNano(0);
        } else if(materialOrderDate.getDayOfWeek() == DayOfWeek.SATURDAY) {
            materialReceiptDate = materialOrderDate.plusDays(2).withHour(cutTime).withMinute(0).withSecond(0).withNano(0);
        } else if(materialOrderDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
            materialReceiptDate = materialOrderDate.plusDays(1).withHour(cutTime).withMinute(0).withSecond(0).withNano(0);
        } else {
            //orderDate가 금요일 오후, 토요일, 일요일도 아니면 당일 12시로 설정
            materialReceiptDate = materialOrderDate.withHour(cutTime).withMinute(0).withSecond(0).withNano(0);
        }

        //원자재 입출고 관리 테이블에서 '같은 원자재'를 가진 애들 중 이력ID가 가장 큰(마지막)인 발주 접수일을 가져온다.;
        //LocalDateTime lastMaterialReceiptDate = materialRepository.find;
        //발주 접수일이 토요일 or 일요일일 수가 있나? 밑에서 계산 잘했다면 없음
        //가장 마지막에 가져온 발주 접수일을 가진 애들을 다시 가져옴, 이력ID가 가장 크고(마지막) 그 가장 큰 애의 발주일이 같은 애들을 가져올 수 있나?;
        //밑에가 한번에 가져오는거
        System.out.println("원자재 뭔데 : " + materialName);
        List<MaterialInOut> materialInOuts = materialRepository.findLatestReceiptForMaterial(materialName.toString());
        System.out.println("리스트 비었냐 : " + materialInOuts.size());
        //System.out.println("리스트 값 : " + materialInOuts.get(0).getMaterialName());

        //list가 isEmpty
        if(materialInOuts.isEmpty()){
            System.out.println("싯팔 여기여 ?");
            //발주할 원자재가 최대 2000을 넘지 않으면 바로 발주
            if (materialAmount < MAX_ORDER_AMOUNT) {
                System.out.println("싯팔  ?"+materialAmount);
                //원자재명, 발주량, 수주번호, 발주일(수주일), 발주 접수일(materialReceiptDate), 상태 set하고 save;
                materialRepository.save(createMaterialOrder(order, materialName, materialAmount, supplierName, materialOrderDate, materialReceiptDate));
            } else {
                //발주해야할 원자재가 2000이 넘었다면 반복해서 발주
                while(materialAmount >= 0) {
                    //ex)5000kg을 발주해야한다면 2000을 2번해야함
                    if(materialAmount >= MAX_ORDER_AMOUNT) {
                        //원자재명, 발주량 = MAX_ORDER_AMOUNT, 수주번호, 발주일(수주일), 발주 접수일(materialReceiptDate), 상태 set하고 save;
                        materialRepository.save(createMaterialOrder(order, materialName, MAX_ORDER_AMOUNT, supplierName, materialOrderDate, materialReceiptDate));
                        materialReceiptDate = materialReceiptDate.plusDays(1);
                    } else {
                        //2000을 계속 빼다가 2000미만이 되면 남은 만큼 발주
                        //원자재명, 발주량 = materialAmount, 수주번호, 발주일(수주일), 발주 접수일(materialReceiptDate), 상태 set하고 save;
                        materialRepository.save(createMaterialOrder(order, materialName, materialAmount, supplierName, materialOrderDate, materialReceiptDate));
                    }

                    materialAmount -= MAX_ORDER_AMOUNT;

                    //발주를 계속하다가 금요일이 되었다면 토,일에는 발주접수가 불가능하니 다시 월요일 12시로 조정
                    if(materialReceiptDate.getDayOfWeek() == DayOfWeek.FRIDAY)
                        materialReceiptDate = materialReceiptDate.plusDays(3).withHour(cutTime).withMinute(0).withSecond(0).withNano(0);
                }
            }
        }

        //가져온 list가 안 비었다면
        else {
            //테이블에서 해당 원자재의 마지막 발주 접수일을 가져옴
            materialReceiptDate = materialRepository.findLatestMaterialInOut(materialName.toString());

            //일단 가져온 list의 발주량의 합을 구한다.
            Long materialAmountSum = 0L;
            for (MaterialInOut materialInOut : materialInOuts) {
                materialAmountSum += materialInOut.getMaterialInoutAmount();
            }
            System.out.println(materialAmountSum);

            /*//그 애들의 발주량의 합이 2000과 같은가?
            if(MaterialOrderAmountSum == 2000) {
                //2000과 같다면 그 날 발주할 수 있는 양을 가득 채웠기 때문에 다음날에 발주접수가 됨
                //근데 금요일이다? 월요일에 발주접수가 됨.
                if(그 날이 금요일?)
                    materialReceiptDate.plusDays(3);

                materialReceiptDate.plusDays(1);
            }
            *//*else if(발주량의 합이 2000을 넘는다?) {
                //이 경우는 생기지 않음
            }*/

            //발주량의 합이 2000이 안넘는다.
            //발주할 수 있는 남은 양 계산
            Long maxMaterialOrderAmount = MAX_ORDER_AMOUNT;
            maxMaterialOrderAmount -= materialAmountSum;
            System.out.println("남은 양 : " + maxMaterialOrderAmount);

            //들어온 양배추량이 max치(발주할 수 있는 남은 양)를 넘는가
            if(materialAmount > maxMaterialOrderAmount) {
                //원자재명, 발주량(maxMaterialOrderAmount), 수주번호, 발주일(수주일), 발주 접수일(materialReceiptDate), 상태 set하고 save;
                materialRepository.save(createMaterialOrder(order, materialName, maxMaterialOrderAmount, supplierName, materialOrderDate, materialReceiptDate));
                materialAmount -= maxMaterialOrderAmount;
                System.out.println("dkdrlahEL: " + materialReceiptDate);
                materialReceiptDate = materialReceiptDate.plusDays(1);
                System.out.println("dkdrlahEL: " + materialReceiptDate.plusDays(1));
            }

            //위에서 발주하고도 2000 이상인지
            if (materialAmount > MAX_ORDER_AMOUNT) {
                //2000을 save하고 2000을 빼면서 2000보다 작아질 때 까지 반복
                while (true) {
                    //최대 수량만큼 발주
                    //원자재명, 발주량(MAX_ORDER_AMOUNT), 수주번호, 발주일(수주일), 발주 접수일(materialReceiptDate), 상태 set하고 save;
                    System.out.println("여기다 이거야: " + materialReceiptDate);
                    materialRepository.save(createMaterialOrder(order, materialName, MAX_ORDER_AMOUNT, supplierName, materialOrderDate, materialReceiptDate));
                    materialAmount -= MAX_ORDER_AMOUNT;
                    materialReceiptDate = materialReceiptDate.plusDays(1);

                    //금요일이 되었다면 다시 월요일로
                    if(materialReceiptDate.getDayOfWeek() == DayOfWeek.FRIDAY){
                        materialReceiptDate.plusDays(3).withHour(cutTime).withMinute(0).withSecond(0).withNano(0); // 다시 월요일로 맞춰주기
                    }

                    //2000보다 작거나 같아지면 발주하고 반복 아웃
                    if(materialAmount <= MAX_ORDER_AMOUNT) {
                        //원자재명, 발주량(materialAmount), 수주번호, 발주일(수주일), 발주 접수일(materialReceiptDate), 상태 set하고 save;
                        materialRepository.save(createMaterialOrder(order, materialName, materialAmount, supplierName, materialOrderDate, materialReceiptDate));
                        break;
                    }
                }
            } else {
                //발주하고 2000 이상이 아니면 바로 발주 가능
                //원자재명, 발주량(materialAmount), 수주번호, 발주일(수주일), 발주 접수일(materialReceiptDate), 상태 set하고 save;
                materialRepository.save(createMaterialOrder(order, materialName, materialAmount, supplierName, materialOrderDate, materialReceiptDate));
            }
        }
    }

    //주 제료량 계산
    private long calculateMaterialAmount(ProductName productName, long productionAmount) {
        switch (productName) {
            case CABBAGE_JUICE:
            case GARLIC_JUICE:
                return (long) Math.ceil(productionAmount * 30 * 10 / 0.5 / 0.2 / 0.75 / 1000);
            case POMEGRANATE_JELLY:
            case PLUM_JELLY:
                return (long) Math.ceil(productionAmount * 25 * 5 / 1000.0);
            default:
                throw new IllegalArgumentException("Invalid product type");
        }
    }

    //부 제료량 계산
    private long calculateSubMaterialAmount(ProductName productName, long productionAmount) {
        switch (productName) {
            case CABBAGE_JUICE:
            case GARLIC_JUICE:
                return (long) Math.ceil(productionAmount * 30 * 5 / 1000.0);
            case POMEGRANATE_JELLY:
            case PLUM_JELLY:
                return (long) Math.ceil(productionAmount * 25 * 2 / 1000.0);
            default:
                throw new IllegalArgumentException("Invalid product type");
        }

    }

    private MaterialInOut createMaterialOrder(Order order, MaterialName materialName, Long materialAmount, MaterialSupplierName materialSupplierName, LocalDateTime materialOrderDate, LocalDateTime materialReceiptDate) {
        MaterialInOut materialInOut = new MaterialInOut();
        materialInOut.setOrder(order);
        materialInOut.setMaterialName(materialName);
        materialInOut.setMaterialInoutAmount(materialAmount);
        materialInOut.setMaterialSupplierName(materialSupplierName);
        materialInOut.setMaterialOrderDate(materialOrderDate);
        materialInOut.setMaterialReceiptDate(materialReceiptDate);
        materialInOut.setMaterialStatus(MaterialStatus.PREPARING_SHIP);
        return materialInOut;
    }

}
